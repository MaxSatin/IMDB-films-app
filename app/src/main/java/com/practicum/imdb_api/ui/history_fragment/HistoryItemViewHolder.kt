package com.practicum.imdb_api.ui.history_fragment


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.imdb_api.databinding.ItemMovieHistoryBinding
import com.practicum.imdb_api.domain.models.movie.Movie

class HistoryItemViewHolder(
    private val binding: ItemMovieHistoryBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie){
        Glide.with(itemView)
            .load(movie.image)
            .into(binding.cover)

        binding.title.text = movie.title
        binding.description.text = movie.description
    }
}