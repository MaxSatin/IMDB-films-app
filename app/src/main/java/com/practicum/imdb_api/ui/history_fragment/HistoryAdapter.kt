package com.practicum.imdb_api.ui.history_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.databinding.ItemMovieHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryItemViewHolder>() {

    var movieHistoryList = ArrayList<Movie>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val binding = ItemMovieHistoryBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HistoryItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}