package com.practicum.imdb_api.ui.persons_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.databinding.ItemPersonBinding
import com.practicum.imdb_api.domain.models.person.Person

class PersonsAdapter() : RecyclerView.Adapter<PersonsViewHolder> () {

     var personList = ArrayList<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonsViewHolder {
        val binding = ItemPersonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonsViewHolder, position: Int) {
        holder.bind(personList.get(position))
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    private fun clearList() {
        personList.clear()
    }
}
