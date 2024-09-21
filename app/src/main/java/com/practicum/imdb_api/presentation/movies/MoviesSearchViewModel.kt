package com.practicum.imdb_api.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.imdb_api.util.Creator
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.Movie

class MoviesSearchViewModel(
    application: Application,
): AndroidViewModel(application) {

    private var latestSearchText: String? = null
    private val moviesInteractor = Creator.provideMoviesInteractor(getApplication<Application>())

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MoviesSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }




    private val movies = ArrayList<Movie>()

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val showToast = SingleLineEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

    private val handler = Handler(Looper.getMainLooper())


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

    override fun onCleared() {
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
                            if (foundMovies != null) {
                                movies.clear()
                                movies.addAll(foundMovies)
                            }
                            when {
                                errorMessage != null -> {
                                    renderState(
                                        MoviesState.Error(
                                            getApplication<Application>().getString(R.string.something_went_wrong)
                                        )
                                    )
                                    showToast.postValue(errorMessage)
                                }

                                movies.isEmpty() -> {
                                    renderState(
                                        MoviesState.Empty(getApplication<Application>().getString(R.string.nothing_found))
                                    )
                                }

                                else -> {
                                    renderState(
                                        MoviesState.Content(movies)
                                    )

                                }
                            }
                    }
                })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }



}