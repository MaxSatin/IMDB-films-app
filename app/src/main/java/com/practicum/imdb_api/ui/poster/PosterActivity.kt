package com.practicum.imdb_api.ui.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.practicum.imdb_api.util.Creator
import com.practicum.imdb_api.R
import com.practicum.imdb_api.databinding.ActivityPosterBinding
import com.practicum.imdb_api.presentation.movie_details.PosterView

class PosterActivity : AppCompatActivity(), PosterView {

    lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val binding = ActivityPosterBinding.inflate(LayoutInflater.from(this))
        setContentView(R.layout.activity_poster)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        poster = findViewById<ImageView>(R.id.poster)

        val intent = intent
        val posterUrl = intent.getStringExtra("poster") ?: ""

        val posterController = Creator.providePosterPresenter(this, posterUrl)

        posterController.loadPoster()

    }

    override fun loadPoster(url: String?) {
        Glide.with(this)
            .load(url)
            .into(poster)
    }
}