package com.practicum.imdb_api.ui.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.practicum.imdb_api.databinding.PosterFragmentBinding
import com.practicum.imdb_api.presentation.movie_details.viewmodel.MoviePosterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PosterFragment() : Fragment() {

    companion object {
        private const val POSTER_URL = "url"
        fun newInstance(movieUrl: String): PosterFragment {
            return PosterFragment().apply {
                arguments = bundleOf(POSTER_URL to movieUrl)
            }
        }
    }

    private val viewModel:MoviePosterViewModel by viewModel()
    private var _binding: PosterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = PosterFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(binding.root)
            .load(arguments?.getString(POSTER_URL))
            .fitCenter()
            .into(binding.poster)
    }

}