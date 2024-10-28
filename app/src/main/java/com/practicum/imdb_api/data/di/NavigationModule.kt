package com.practicum.imdb_api.data.di

import com.practicum.imdb_api.core.navigation.Router
import com.practicum.imdb_api.core.navigation.RouterImpl
import org.koin.dsl.module

val navigationModule = module {
    val router = RouterImpl()

    single<Router> { router }
    single { router.navigatorHolder }
}