package com.practicum.imdb_api.ui.movies

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.models.movie.Movie

class MovieViewHolder(
    parent: ViewGroup,
    private val clickListener: MoviesAdapter.OnMovieClickListener,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(
            R.layout.item_movie,
            parent,
            false
        )
) {

    var movieName = itemView.findViewById<TextView>(R.id.movieName)
    var movieDescription = itemView.findViewById<TextView>(R.id.movieDescription)
    var moviePoster = itemView.findViewById<ImageView>(R.id.moviePoster)
    var favorite = itemView.findViewById<ImageView>(R.id.favorite)

    var inFavoriteToggle: ImageView? = itemView.findViewById(R.id.favorite)

    fun bind(movie: Movie) {
        favorite.setImageResource( getFavoriteToggleDrawable(movie.inFavorite))
        movieName.text = movie.title
        movieDescription.text = movie.description
        Glide.with(itemView)
            .load(movie.image)
            .fitCenter()
            .into(moviePoster)


        itemView.setOnClickListener {
            clickListener.onFavoriteToggleClick(movie)
            clickListener.onMovieClick(movie)
        }

    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Int {
        val icon = when (inFavorite) {
            true -> R.drawable.active_favorite
            else -> R.drawable.inactive_favorite
        }
        return icon
    }
}