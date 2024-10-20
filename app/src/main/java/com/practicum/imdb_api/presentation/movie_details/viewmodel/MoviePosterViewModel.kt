package com.practicum.imdb_api.presentation.movie_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.imdb_api.presentation.movie_details.state.MoviesPosterState

class MoviePosterViewModel(
    private val moviePosterUrl: String
): ViewModel() {

    private val posterStateLiveData = MutableLiveData<MoviesPosterState>()
    fun observePosterStateLiveData(): LiveData<MoviesPosterState> = posterStateLiveData

    private fun renderState(state:MoviesPosterState) {
        posterStateLiveData.postValue(state)
    }

    init {
        renderState(
            MoviesPosterState.Loading
        )
        renderState(MoviesPosterState.Content(moviePosterUrl))
    }
}