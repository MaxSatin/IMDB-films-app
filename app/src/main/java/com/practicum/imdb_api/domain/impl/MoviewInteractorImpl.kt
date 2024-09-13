package com.practicum.imdb_api.domain.impl

import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
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
}
