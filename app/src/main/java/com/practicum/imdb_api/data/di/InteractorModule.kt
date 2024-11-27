package com.practicum.imdb_api.data.di

import com.practicum.imdb_api.data.HistoryRepositoryImpl
import com.practicum.imdb_api.data.MoviesRepositoryImpl
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.db.HistoryInteractor
import com.practicum.imdb_api.domain.impl.HistoryInteractorImpl
import com.practicum.imdb_api.domain.impl.MoviesInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single <MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get(), get())
    }

    single <MoviesInteractor> {
        MoviesInteractorImpl(get())
    }

    single <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
}