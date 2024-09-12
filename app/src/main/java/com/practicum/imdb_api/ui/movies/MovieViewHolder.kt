package com.practicum.imdb_api.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.models.Movie

class MovieViewHolder (parent : ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
    .inflate(R.layout.item_movie, parent, false)) {

    var movieName = itemView.findViewById<TextView>(R.id.movieName)
    var movieDescription = itemView.findViewById<TextView>(R.id.movieDescription)
    var moviePoster = itemView.findViewById<ImageView>(R.id.moviePoster)

    fun bind(movie: Movie) {
        movieName.text = movie.title
        movieDescription.text = movie.description
        Glide.with(itemView)
            .load(movie.image)
            .fitCenter()
            .into(moviePoster)
    }
}