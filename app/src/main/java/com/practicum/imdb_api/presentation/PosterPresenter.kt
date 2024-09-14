package com.practicum.imdb_api.presentation

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.practicum.imdb_api.R
import com.practicum.imdb_api.presentation.poster.PosterView

class PosterPresenter(
    private val view: PosterView,
    private val posterUrl: String
) {

    fun loadPoster(){
        view.loadPoster(posterUrl)
    }
}