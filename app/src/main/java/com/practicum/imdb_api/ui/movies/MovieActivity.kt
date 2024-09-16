package com.practicum.imdb_api.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.presentation.movies.MoviesSearchPresenter
import com.practicum.imdb_api.presentation.movies.MoviesState
import com.practicum.imdb_api.presentation.movies.MoviesView
import com.practicum.imdb_api.ui.poster.PosterActivity
import com.practicum.imdb_api.MoviesApplication
import com.practicum.imdb_api.util.Creator

class MovieActivity : AppCompatActivity(), MoviesView {

    companion object {
        private const val CLICK_DEBOUNE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }
    private var moviesSearchPresenter: MoviesSearchPresenter? = null

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

//    lateinit var binding: ActivityMainBinding

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.textSearch)
        moviesList = findViewById(R.id.recyclerMovie)
        progressBar = findViewById(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        moviesSearchPresenter = (this.application as? MoviesApplication)?.moviesSearchPresenter

        if (moviesSearchPresenter == null) {
            moviesSearchPresenter = Creator.provideMoviesSearchPresenter(
                this.applicationContext
            )
            (this.application as? MoviesApplication)?.moviesSearchPresenter = moviesSearchPresenter
        }




        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moviesSearchPresenter?.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let {
            queryInput.addTextChangedListener(it)
        }

    }

    override fun onStart() {
        super.onStart()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        moviesSearchPresenter?.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
        moviesSearchPresenter?.onDestroy()
        moviesSearchPresenter?.detachView()

        if (isFinishing){
            (this.application as? MoviesApplication)?.moviesSearchPresenter = null
        }
    }

    override fun onPause() {
        super.onPause()
        moviesSearchPresenter?.detachView()
    }

    override fun onStop() {
        super.onStop()
        moviesSearchPresenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesSearchPresenter?.detachView()
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNE_DELAY)
        }
        return current
    }

    fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun render(state: MoviesState) {
        when (state){
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}

