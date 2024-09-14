package com.practicum.imdb_api.presentation.poster

import android.content.Context

interface PosterView {
    fun loadPoster(url: String?)
}