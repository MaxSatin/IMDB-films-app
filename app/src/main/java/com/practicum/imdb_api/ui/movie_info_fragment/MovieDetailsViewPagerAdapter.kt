package com.practicum.imdb_api.ui.movie_info_fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MovieDetailsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val posterUrl: String,
    private val movieId: String
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PosterFragment.newInstance(posterUrl)
            1 -> MovieDetailsFragment.newInstance(movieID = movieId)
            else -> PosterFragment.newInstance("")
        }
    }
}