package com.practicum.imdb_api.presentation.movies.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.presentation.movies.state.MoviesState
import com.practicum.imdb_api.presentation.movies.SingleLineEvent
import com.practicum.imdb_api.presentation.persons.viewmodel.PersonsViewModel
import com.practicum.imdb_api.presentation.persons.viewmodel.PersonsViewModel.Companion
import debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesSearchViewModel(
    application: Application,
    private val moviesInteractor: MoviesInteractor,
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
//        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                MoviesSearchViewModel(this[APPLICATION_KEY] as Application)
//            }
//        }
    }

    private var latestSearchText: String? = null
    private var job: Job? = null
    private val movieSearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { text ->
        searchRequest(text)
    }


    private val movies = ArrayList<Movie>()

    private val stateLiveData = MutableLiveData<MoviesState>()
    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->

        liveData.addSource(stateLiveData) { movieState ->
            liveData.value = when (movieState) {

                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
                is MoviesState.Empty -> movieState
                is MoviesState.Error -> movieState
                is MoviesState.Loading -> movieState
            }
        }

    }

    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private val showToast = SingleLineEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

    private val handler = Handler(Looper.getMainLooper())

    fun toggleFavorite(movie: Movie) {
        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie)
        } else {
            moviesInteractor.addMovieToFavorites(movie)
        }

        updateMovieContent(movie.id, movie.copy(inFavorite = !movie.inFavorite))
    }

    private fun updateMovieContent(movieId: String, newMovie: Movie) {
        val currentState = stateLiveData.value

        if (currentState is MoviesState.Content) {
            val movieIndex = currentState.movies.indexOfFirst { it.id == movieId }
            if (movieIndex != -1) {
                stateLiveData.value = MoviesState.Content(
                    currentState.movies.toMutableList().also { it[movieIndex] = newMovie }
                )
            }
        }
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        movieSearchDebounce(changedText)
//        job?.cancel()
//        job = viewModelScope.launch {
//
////            delay(SEARCH_DEBOUNCE_DELAY)
////            searchRequest(changedText)
//        }

//        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
//        val searchRunnable = Runnable { searchRequest(changedText) }
//        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
//        handler.postAtTime(
//            searchRunnable,
//            SEARCH_REQUEST_TOKEN,
//            postTime,
//        )
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {

            renderState(
                MoviesState.Loading
            )
            viewModelScope.launch {
                moviesInteractor.searchMovies(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundMovies: List<Movie>?, errorMessage: String?) {
        val movies = mutableListOf<Movie>()
        if (foundMovies != null) {
            movies.addAll(foundMovies)
        }
        when {
            errorMessage != null -> {
                renderState(
                    MoviesState.Error(
                        getApplication<Application>().getString(R.string.something_went_wrong)
                    )
                )
                showToast.postValue(errorMessage!!)
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
//
//                                else -> {
//                                    renderState(
//                                        MoviesState.Content(movies)
//                                    )
//
//                                }
    }
//                object : MoviesInteractor.MoviesConsumer {
//                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
//                            if (foundMovies != null) {
//                                movies.clear()
//                                movies.addAll(foundMovies)
//                            }
//                            when {
//                                errorMessage != null -> {
//                                    renderState(
//                                        MoviesState.Error(
//                                            getApplication<Application>().getString(R.string.something_went_wrong)
//                                        )
//                                    )
//                                    showToast.postValue(errorMessage!!)
//                                }
//
//                                movies.isEmpty() -> {
//                                    renderState(
//                                        MoviesState.Empty(getApplication<Application>().getString(R.string.nothing_found))
//                                    )
//                                }
//
//                                else -> {
//                                    renderState(
//                                        MoviesState.Content(movies)
//                                    )
//
//                                }
//                            }
//                    }
//                })
//        }


    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }


}