package com.practicum.imdb_api.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.imdb_api.data.MoviesRepositoryImpl
import com.practicum.imdb_api.data.network.RetrofitNetworkClient
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.impl.MoviesInteractorImpl
import com.practicum.imdb_api.presentation.movies.MoviesSearchPresenter
import com.practicum.imdb_api.presentation.PosterPresenter
import com.practicum.imdb_api.presentation.movies.MoviesView
import com.practicum.imdb_api.presentation.poster.PosterView

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun provideMoviesSearchPresenter(
        activity: MoviesView,
        context: Context,
    ): MoviesSearchPresenter {
        return MoviesSearchPresenter(activity, context)
    }

    fun providePosterPresenter(activity: PosterView, url: String): PosterPresenter {
        return PosterPresenter(activity, url)
    }
}