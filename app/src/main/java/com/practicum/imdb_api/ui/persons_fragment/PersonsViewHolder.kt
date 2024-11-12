package com.practicum.imdb_api.ui.persons_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.imdb_api.R
import com.practicum.imdb_api.databinding.ItemPersonBinding
import com.practicum.imdb_api.domain.models.person.Person

class PersonsViewHolder(
    private val binding: ItemPersonBinding,
) : RecyclerView.ViewHolder(binding.root)
 {
    fun bind(person: Person) {
        binding.personName.text = person.title
        binding.personDescription.text = person.description
        Glide.with(itemView)
            .load(person.image)
            .circleCrop()
            .into(binding.personPhoto)
    }
}