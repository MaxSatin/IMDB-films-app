package com.practicum.imdb_api.presentation.movie_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.details.MovieDetails
import com.practicum.imdb_api.presentation.movie_details.state.MoviesDetailsState

class MovieDetailsViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor,
) : ViewModel() {

    private val movieDetailsStateLiveData = MutableLiveData<MoviesDetailsState>()
    fun getMovieDetailsLiveData(): LiveData<MoviesDetailsState> = movieDetailsStateLiveData

    init {
        renderState(MoviesDetailsState.Loading)
        getMoviesDetails()
    }

    private fun getMoviesDetails() {
        renderState(
            MoviesDetailsState.Loading
        )
        moviesInteractor.getMovieDetails(
            movieId,
            object : MoviesInteractor.MovieDetailsConsumer {
                override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
                    when {
                        errorMessage != null -> renderState(
                            MoviesDetailsState.Error("Что-то пошло не так")
                        )

                        movieDetails == null ->
                            renderState(
                                MoviesDetailsState.Empty("Информация о фильме отсутствует")
                            )

                        else -> renderState(
                            MoviesDetailsState.Content(movieDetails)
                        )
                    }
                }
            })
    }

    private fun renderState(state: MoviesDetailsState) {
        movieDetailsStateLiveData.postValue(state)

    }


}