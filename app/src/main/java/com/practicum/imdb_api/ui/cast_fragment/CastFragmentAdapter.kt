package com.practicum.imdb_api.ui.cast_fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.imdb_api.R
import com.practicum.imdb_api.domain.models.cast_members.CastInfo

class CastFragmentAdapter(
    private val castInfo: CastInfo,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val castList: List<CastMemberItem> = getGeneralCastList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TYPE_DIRECTORS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
                return BaseViewHolder.DirectorViewHolder(view)
            }

            VIEW_TYPE_WRITERS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
                return BaseViewHolder.WritersViewHolder(view)
            }

            VIEW_TYPE_ACTORS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
                return BaseViewHolder.ActorsViewHolder(view)
            }

            VIEW_TYPE_DIRECTORS_PH -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.director_item, parent, false)
                return BaseViewHolder.DirectorPH(view)
            }

            VIEW_TYPE_WRITERS_PH -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.writers_item, parent, false)
                return BaseViewHolder.WritersPH(view)
            }

            VIEW_TYPE_ACTORS_PH -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.actors_item, parent, false)
                return BaseViewHolder.ActorsPH(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")

        }
    }


    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BaseViewHolder.DirectorViewHolder -> {
                holder.bind(castList[position] as CastMemberItem.Director)
                Log.d("DirectorViewHolder", "${castList[position]}")
            }
            is BaseViewHolder.WritersViewHolder -> holder.bind(castList[position] as CastMemberItem.Writers)
            is BaseViewHolder.ActorsViewHolder -> holder.bind(castList[position] as CastMemberItem.Actors)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (castList[position]) {
            is CastMemberItem.Director -> VIEW_TYPE_DIRECTORS
            is CastMemberItem.Writers -> VIEW_TYPE_WRITERS
            is CastMemberItem.Actors -> VIEW_TYPE_ACTORS
            is CastMemberItem.DirectorPH -> VIEW_TYPE_DIRECTORS_PH
            is CastMemberItem.WritersPH -> VIEW_TYPE_WRITERS_PH
            else -> VIEW_TYPE_ACTORS_PH
        }
    }

    private fun getGeneralCastList(): MutableList<CastMemberItem> {
        val castList = mutableListOf<CastMemberItem>()
        castList.add(CastMemberItem.DirectorPH())
        castInfo.directors.items.forEach { directorInfo ->
            castList.add(
                CastMemberItem.Director(
                    id = directorInfo.id,
                    name = directorInfo.name,
                    description = directorInfo.description
                )
            )
        }
        castList.add(CastMemberItem.WritersPH())
        castInfo.writers.items.forEach { writersInfo ->
            castList.add(
                CastMemberItem.Writers(
                    id = writersInfo.id,
                    name = writersInfo.name,
                    description = writersInfo.description
                )
            )
        }
        castList.add(CastMemberItem.ActorsPH())
        castInfo.actors.forEach { actor ->
            castList.add(
                CastMemberItem.Actors(
                    id = actor.id,
                    image = actor.image,
                    name = actor.name,
                    asCharacter = actor.asCharacter
                )
            )
        }
        Log.d("castList", "$castList")
        return castList
    }

    companion object {
        const val VIEW_TYPE_DIRECTORS = 0
        const val VIEW_TYPE_WRITERS = 1
        const val VIEW_TYPE_ACTORS = 2
        const val VIEW_TYPE_DIRECTORS_PH = 3
        const val VIEW_TYPE_WRITERS_PH = 4
        const val VIEW_TYPE_ACTORS_PH = 5
    }
}