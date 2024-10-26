package com.practicum.imdb_api.domain.impl

import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.util.Resource
import java.util.concurrent.Executors

class MoviesInteractorImpl(private val repository: MoviesRepository) : MoviesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchMovies(expression: String, consumer: MoviesInteractor.MoviesConsumer) {
        executor.execute {
            when (val resourse = repository.searchMovies(expression)) {
                is Resource.Success -> consumer.consume(resourse.data, null)
                is Resource.Error -> consumer.consume(null, resourse.message)
            }

        }
    }

    override fun getMovieDetails(movieId: String, consumer: MoviesInteractor.MovieDetailsConsumer) {
        executor.execute {
            when (val resourse = repository.getMovieDetails(movieId)) {
                is Resource.Success -> consumer.consume(resourse.data, null)
                is Resource.Error -> consumer.consume(null, resourse.message)
            }
        }
    }

    override fun getCastInfo(movieId: String, consumer: MoviesInteractor.CastInfoConsumer) {
        executor.execute {
            when(val resource = repository.getCastList(movieId)){
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }

    override fun addMovieToFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }
}
