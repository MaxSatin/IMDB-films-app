package com.practicum.imdb_api.ui.cast_fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.imdb_api.databinding.CastItemBinding

sealed class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    class DirectorViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val binding = CastItemBinding.bind(itemView)

        fun bind(director: CastMemberItem.Director) {
            binding.name.text = director.name
            binding.additional.text = director.description
            binding.asCharacter.isVisible = false
        }
    }

    class WritersViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val binding = CastItemBinding.bind(itemView)
        fun bind(writers: CastMemberItem.Writers) {
            binding.name.text = writers.name
            binding.additional.text = writers.description
            binding.asCharacter.isVisible = false
        }
    }

    class ActorsViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val binding = CastItemBinding.bind(itemView)
        fun bind(actors: CastMemberItem.Actors) {
            binding.photo.isVisible = true
            binding.additional.isVisible = false
            binding.name.text = actors.name
            Glide.with(itemView)
                .load(actors.image)
                .fitCenter()
                .into(binding.photo)
            binding.asCharacter.text = actors.asCharacter
        }
    }

    class DirectorPH(itemView: View) : BaseViewHolder(itemView)
    class WritersPH(itemView: View) : BaseViewHolder(itemView)
    class ActorsPH(itemView: View) : BaseViewHolder(itemView)
}

