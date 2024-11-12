package com.practicum.imdb_api.presentation.persons.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.presentation.movies.SingleLineEvent
import com.practicum.imdb_api.presentation.movies.state.MoviesState
import com.practicum.imdb_api.presentation.persons.state.PersonsState

class PersonsViewModel(
    application: Application,
    private val moviesInteractor: MoviesInteractor,
) : AndroidViewModel(application) {

    private var latestSearchText: String? = null


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val persons = ArrayList<Person>()

    private val stateLiveData = MutableLiveData<PersonsState>()
//    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->
//
//        liveData.addSource(stateLiveData) { movieState ->
//            liveData.value = when (movieState) {
//
//                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
//                is MoviesState.Empty -> movieState
//                is MoviesState.Error -> movieState
//                is MoviesState.Loading -> movieState
//            }
//        }
//
//    }

    fun observeState(): LiveData<PersonsState> = stateLiveData

    private val showToast = SingleLineEvent<String>()
    fun observeToastState(): LiveData<String> = showToast

    private val handler = Handler(Looper.getMainLooper())


    fun searchDebounce(changedText: String) {

        if (latestSearchText == changedText) {
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
                PersonsState.Loading
            )
            moviesInteractor.searchPersons(
                newSearchText,
                object : MoviesInteractor.PersonsConsumer {
                    override fun consume(foundPersons: List<Person>?, errorMessage: String?) {
                        if (foundPersons != null) {
                            persons.clear()
                            persons.addAll(foundPersons)
                        }
                        when {
                            errorMessage != null -> {
                                renderState(
                                    PersonsState.Error(
                                        getApplication<Application>().getString(R.string.something_went_wrong)
                                    )
                                )
                                showToast.postValue(errorMessage!!)
                            }

                            persons.isEmpty() -> {
                                renderState(
                                    PersonsState.Empty(getApplication<Application>().getString(R.string.nothing_found))
                                )
                            }

                            else -> {
                                renderState(
                                    PersonsState.Content(persons)
                                )

                            }
                        }
                    }
                })
        }
    }

    private fun renderState(state: PersonsState) {
        stateLiveData.postValue(state)
    }
}

