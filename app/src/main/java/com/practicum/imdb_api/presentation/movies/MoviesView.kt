package com.practicum.imdb_api.presentation.movies

import com.practicum.imdb_api.domain.models.Movie

interface MoviesView {

    fun render(state: MoviesState)

    fun showToast(additionalMessage: String)
}