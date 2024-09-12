package com.practicum.imdb_api.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.Creator
import com.practicum.imdb_api.R
import com.practicum.imdb_api.data.dto.MoviesSearchResponse
import com.practicum.imdb_api.data.network.IMDbApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.practicum.imdb_api.databinding.ActivityMainBinding
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.ui.poster.PosterActivity

class MovieActivity : AppCompatActivity() {
    private val moviesInteractor = Creator.provideMoviesInteractor()

    companion object {
        private const val CLICK_DEBOUNE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { showMovies() }

    private val baseImdbUrl = "https://tv-api.com"
    private val token = "k_zcuw1ytf"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseImdbUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(IMDbApiService::class.java)
    private val movies = ArrayList<Movie>()
    private val adapter = MovieAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var recyclerMovie: RecyclerView
    private lateinit var placeHolder: TextView
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        searchButton = findViewById<Button>(R.id.searchButton)
        queryInput = findViewById<EditText>(R.id.textSearch)
        recyclerMovie = findViewById<RecyclerView>(R.id.recyclerMovie)
        placeHolder = findViewById(R.id.placeholderMessage)

        adapter.movies = movies
        recyclerMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerMovie.adapter = adapter

        queryInput.addTextChangedListener (
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchDebounce()
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )

        searchButton.setOnClickListener {
            if (queryInput.text.isNotEmpty()) {
                showMovies()
            } else {
                Toast.makeText(applicationContext, "Пустой запрос", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNE_DELAY)
        }
        return current
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeHolder.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeHolder.text = text
        }
        if (additionalMessage.isNotEmpty()) {
            Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG).show()
        } else {
            placeHolder.visibility = View.GONE
        }
    }

    private fun hideMessage() {
        placeHolder.visibility = View.GONE
    }

    private fun showMovies() {
        if (queryInput.text.isNotEmpty()) {

            binding.placeholderMessage.visibility = View.GONE
            recyclerMovie.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            moviesInteractor.searchMovies(queryInput.text.toString(), object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>) {
                    handler.post {
                        binding.progressBar.visibility = View.GONE
                        movies.clear()
                        movies.addAll(foundMovies)
                        recyclerMovie.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                        if (movies.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                        } else {
                            hideMessage()
                        }
                    }
                }
            })
        }
    }
}

