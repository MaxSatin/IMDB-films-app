package com.practicum.imdb_api.presentation

import com.practicum.imdb_api.presentation.movie_details.PosterView

class PosterPresenter(
    private val view: PosterView,
    private val posterUrl: String
) {

    fun loadPoster(){
        view.loadPoster(posterUrl)
    }
}