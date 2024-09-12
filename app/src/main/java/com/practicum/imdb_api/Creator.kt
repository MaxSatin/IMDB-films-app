package com.practicum.imdb_api

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.practicum.imdb_api.data.MoviesRepositoryImpl
import com.practicum.imdb_api.data.network.RetrofitNetworkClient
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.impl.MoviesInteractorImpl
import com.practicum.imdb_api.presentation.MoviesSearchController
import com.practicum.imdb_api.presentation.PosterController
import com.practicum.imdb_api.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }

    fun provideMoviesSearchController(
        activity: AppCompatActivity,
        adapter: MoviesAdapter
    ): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }

    fun providePosterController(activity: AppCompatActivity): PosterController {
        return PosterController(activity)
    }
}