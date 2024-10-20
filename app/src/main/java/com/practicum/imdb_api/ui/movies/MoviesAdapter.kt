package com.practicum.imdb_api.ui.movies

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.domain.models.Movie

class MoviesAdapter (private val clickListener: OnMovieClickListener) : RecyclerView.Adapter<MovieViewHolder> () {

    var movies = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent, clickListener)
    }

    override fun getItemCount(): Int {
       return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies.get(position))
    }

    interface OnMovieClickListener {
       fun onMovieClick(movie: Movie)
       fun onFavoriteToggleClick(movie: Movie)
    }
}