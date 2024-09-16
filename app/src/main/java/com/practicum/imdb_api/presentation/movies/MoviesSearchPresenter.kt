package com.practicum.imdb_api.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import com.practicum.imdb_api.util.Creator
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.Movie
import com.practicum.imdb_api.ui.movies.MoviesAdapter

class MoviesSearchPresenter(
    private val context: Context,
) {

    private var view: MoviesView? = null
    private var state: MoviesState? = null
    private var latestSearchText: String? = null
    private val moviesInteractor = Creator.provideMoviesInteractor(context)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }


    private val movies = ArrayList<Movie>()

    private val handler = Handler(Looper.getMainLooper())

    fun attachView(view: MoviesView){
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView(){
        this.view = null
    }

    fun searchDebounce(changedText: String) {

        if(latestSearchText == changedText){
            return
        }

        this.latestSearchText = changedText

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(
                MoviesState.Loading
            )
            moviesInteractor.searchMovies(
                newSearchText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            if (foundMovies != null) {
                                movies.clear()
                                movies.addAll(foundMovies)
                            }
                            when {
                                errorMessage != null -> {
                                    renderState(
                                        MoviesState.Error(
                                            context.getString(R.string.something_went_wrong)
                                        )
                                    )
                                    view?.showToast(errorMessage)
                                }

                                movies.isEmpty() -> {
                                    renderState(
                                        MoviesState.Empty(context.getString(R.string.nothing_found))
                                    )
                                }

                                else -> {
                                    renderState(
                                        MoviesState.Content(movies)
                                    )

                                }
                            }
                        }
                    }
                })
        }
    }

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }

}