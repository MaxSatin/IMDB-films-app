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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.R
import com.practicum.imdb_api.databinding.ActivityMainBinding
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.presentation.movies.MoviesSearchViewModel
import com.practicum.imdb_api.presentation.movies.MoviesState
import com.practicum.imdb_api.ui.poster.PosterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private val adapter = MoviesAdapter (
        object : MoviesAdapter.OnMovieClickListener{
        override fun onMovieClick(movie: Movie) {
            if (clickDebounce()) {
                val intent = Intent(this@MovieActivity, PosterActivity::class.java)
                intent.putExtra("poster", movie.image)
                intent.putExtra("movieDetails",movie.id )
                startActivity(intent)
            }
        }
        override fun onFavoriteToggleClick(movie: Movie){
            viewModel.toggleFavorite(movie)
        }
    }
)
//    @InjectPresenter
//    lateinit var moviesSearchPresenter: MoviesSearchViewModel

//    @ProvidePresenter
//    fun provideMoviesSearchPresenter(): MoviesSearchViewModel {
//       return Creator.provideMoviesSearchPresenter(
//            this.applicationContext
//        )
//    }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    lateinit var binding: ActivityMainBinding

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    val viewModel: MoviesSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this, MoviesSearchViewModel.getViewModelFactory())[MoviesSearchViewModel::class.java]
        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.textSearch)
        moviesList = findViewById(R.id.recyclerMovie)
        progressBar = findViewById(R.id.progressBar)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let {
            queryInput.addTextChangedListener(it)
        }

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeToastState().observe(this) { toast ->
            showToast(toast)
        }
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

    private fun render(state: MoviesState) {
        when (state){
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}

