package com.practicum.imdb_api.ui.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.imdb_api.databinding.MovieDetailsFragmentBinding
import com.practicum.imdb_api.domain.models.MovieDetails
import com.practicum.imdb_api.presentation.movie_details.state.MoviesDetailsState
import com.practicum.imdb_api.presentation.movie_details.viewmodel.MovieDetailsViewModel
import org.koin.core.parameter.parametersOf

class MovieDetailsFragment: Fragment() {
    companion object {
        private const val MOVIE_ID = "movie_id"
        fun newInstance(movieID: String): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = bundleOf(MOVIE_ID to movieID)

            }
        }
    }

    private val viewModel: MovieDetailsViewModel by viewModel{
        parametersOf(arguments?.getString(MOVIE_ID))
    }

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieDetailsLiveData().observe(viewLifecycleOwner){ movieDetailsState ->
            if (movieDetailsState is MoviesDetailsState.Content) {
              val moviesDetails = movieDetailsState.moviesDetails

            }


        }
    }
}