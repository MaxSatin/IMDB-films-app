package com.practicum.imdb_api.data.di

import android.content.Context
import androidx.room.Room
import com.practicum.imdb_api.data.MoviesRepositoryImpl
import com.practicum.imdb_api.data.NetworkClient
import com.practicum.imdb_api.data.Storage.LocalStorage
import com.practicum.imdb_api.data.db.AppDataBase
import com.practicum.imdb_api.data.network.IMDbApiService
import com.practicum.imdb_api.data.network.RetrofitNetworkClient
import com.practicum.imdb_api.domain.api.MoviesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single <IMDbApiService> {
    Retrofit.Builder()
        .baseUrl("https://tv-api.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IMDbApiService::class.java)
    }

    single <NetworkClient> {
        RetrofitNetworkClient(androidContext(), get())
    }

//    single <MoviesRepository> {
//        MoviesRepositoryImpl(get(), get())
//    }
//
//    single { LocalStorage(get()) }

    single {
        androidContext()
            .getSharedPreferences(
                "local_storage", Context.MODE_PRIVATE
            )
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db").build()
    }

}