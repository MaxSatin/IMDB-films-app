package com.practicum.imdb_api.ui.movie_info_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator

import com.practicum.imdb_api.databinding.MovieInfoFragmentBinding

class MovieInfoFragment : Fragment() {

    lateinit var poster: ImageView
    lateinit var tabMediator: TabLayoutMediator

    private var _binding: MovieInfoFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {

        private const val MOVIE_POSTER = "poster"
        private const val MOVIE_DETAILS = "movieDetails"

        fun createArgs (poster: String?, movieId: String?): Bundle {
            return  bundleOf(
                    MOVIE_POSTER to poster,
                    MOVIE_DETAILS to movieId
                )
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieInfoFragmentBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MovieDetailsViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            arguments?.getString("poster") ?: "",
            arguments?.getString("movieDetails") ?: "",
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Постер"
                1 -> tab.text = "Детали"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}