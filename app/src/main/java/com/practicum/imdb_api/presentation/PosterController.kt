package com.practicum.imdb_api.presentation

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.practicum.imdb_api.R

class PosterController(private val activity: AppCompatActivity) {

    private lateinit var poster: ImageView

    fun onCreate() {
        poster = activity.findViewById(R.id.poster)
        val url = activity.intent.extras?.getString("poster", "")

        Glide.with(activity.applicationContext)
            .load(url)
            .into(poster)
    }
}