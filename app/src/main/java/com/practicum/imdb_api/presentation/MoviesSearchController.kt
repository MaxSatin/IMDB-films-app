    package com.practicum.imdb_api.presentation

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
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.practicum.imdb_api.util.Creator
    import com.practicum.imdb_api.R
    import com.practicum.imdb_api.domain.api.MoviesInteractor
    import com.practicum.imdb_api.domain.models.Movie
    import com.practicum.imdb_api.ui.movies.MoviesAdapter

    class MoviesSearchController(
        private val activity: AppCompatActivity,
        private val adapter: MoviesAdapter

    ) {

        private val moviesInteractor = Creator.provideMoviesInteractor(activity)

        companion object {
            private const val SEARCH_DEBOUNCE_DELAY = 2000L
        }

        private lateinit var queryInput: EditText
        private lateinit var placeholderMessage: TextView
        private lateinit var moviesList: RecyclerView
        private lateinit var progressBar: ProgressBar

        private val movies = ArrayList<Movie>()

        private val handler = Handler(Looper.getMainLooper())

        private val searchRunnable = Runnable { searchRequest() }

        private fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        fun onCreate() {
            placeholderMessage = activity.findViewById(R.id.placeholderMessage)
            queryInput = activity.findViewById(R.id.textSearch)
            moviesList = activity.findViewById(R.id.recyclerMovie)
            progressBar = activity.findViewById(R.id.progressBar)

            adapter.movies = movies

            moviesList.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            moviesList.adapter = adapter

            queryInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    searchDebounce()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }

        fun onDestroy() {
            handler.removeCallbacks(searchRunnable)
        }

        private fun searchRequest() {
            if (queryInput.text.isNotEmpty()) {

                placeholderMessage.visibility = View.GONE
                moviesList.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                moviesInteractor.searchMovies(
                    queryInput.text.toString(),
                    object : MoviesInteractor.MoviesConsumer {
                        override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                            handler.post {
                                progressBar.visibility = View.GONE
                                if (foundMovies != null) {
                                    movies.clear()
                                    movies.addAll(foundMovies)
                                    moviesList.visibility = View.VISIBLE
                                    adapter.notifyDataSetChanged()
                                }
                                if (errorMessage != null) {
                                    showMessage(
                                        activity.getString(R.string.something_went_wrong),
                                        errorMessage
                                    )
                                } else if (movies.isEmpty()) {
                                    showMessage(activity.getString(R.string.nothing_found), "")
                                } else {
                                    hideMessage()
                                }
                            }
                        }
                    })
            }
        }

        private fun showMessage(text: String, additionalMessage: String) {
            if (text.isNotEmpty()) {
                placeholderMessage.visibility = View.VISIBLE
                movies.clear()
                adapter.notifyDataSetChanged()
                placeholderMessage.text = text
                if (additionalMessage.isNotEmpty()) {
                    Toast.makeText(activity, additionalMessage, Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                placeholderMessage.visibility = View.GONE
            }
        }

        private fun hideMessage() {
            placeholderMessage.visibility = View.GONE
        }
    }