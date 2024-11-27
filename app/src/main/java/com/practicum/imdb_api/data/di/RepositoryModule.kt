package com.practicum.imdb_api.data.di

import com.practicum.imdb_api.data.HistoryRepositoryImpl
import com.practicum.imdb_api.data.MoviesRepositoryImpl
import com.practicum.imdb_api.data.Storage.LocalStorage
import com.practicum.imdb_api.data.converters.MovieDbConvertor
import com.practicum.imdb_api.domain.api.MoviesRepository
import com.practicum.imdb_api.domain.db.HistoryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single <MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get(), get())
    }

    single <HistoryRepository> {
        HistoryRepositoryImpl(get(),get())
    }

    single { LocalStorage(get()) }

    factory { MovieDbConvertor() }
}