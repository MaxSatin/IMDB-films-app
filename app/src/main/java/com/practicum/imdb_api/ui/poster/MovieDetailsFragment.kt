package com.practicum.imdb_api.ui.poster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.imdb_api.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.imdb_api.databinding.MovieDetailsFragmentBinding
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.presentation.movie_details.state.CastInfoState
import com.practicum.imdb_api.presentation.movie_details.state.MoviesDetailsState
import com.practicum.imdb_api.presentation.movie_details.viewmodel.CastInfoViewModel
import com.practicum.imdb_api.presentation.movie_details.viewmodel.MovieDetailsViewModel
import com.practicum.imdb_api.ui.cast_activity.CastActivity
import org.koin.core.parameter.parametersOf

class MovieDetailsFragment : Fragment() {
    companion object {
        private const val MOVIE_ID = "movie_id"
        private const val MOVIE_TITLE = "movie_title"
        fun newInstance(movieID: String): MovieDetailsFragment {
            return MovieDetailsFragment().apply {
                arguments = bundleOf(MOVIE_ID to movieID)

            }
        }
    }

    private val viewModel: MovieDetailsViewModel by viewModel {
        parametersOf(arguments?.getString(MOVIE_ID))
    }

    private var filmTitle: String? = null

//    private val viewModelCast: CastInfoViewModel by viewModel {
//        parametersOf(arguments?.getString(MOVIE_ID))
//    }

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModelCast.getCastInfo()
        viewModel.getMovieDetailsLiveData().observe(viewLifecycleOwner) { movieDetailsState ->
            when (movieDetailsState) {
                is MoviesDetailsState.Content -> {
                    binding.movieDetailsInfo.isVisible = true
                    with(movieDetailsState.moviesDetails) {
                        filmTitle = title
                        binding.filmTitle.text = title
                        binding.actualYear.text = year
                        binding.actualRating.text = imDbRating
                        binding.actualCountry.text = countries
                        binding.actualGenre.text = genres
                        binding.actualDirector.text = directors
                        binding.actualScreenWriter.text = writers
                        binding.actualActors.text = stars
                        binding.actualStory.text = plot
                    }
                }

                is MoviesDetailsState.Error -> {
                    binding.movieDetailsInfo.isVisible = false
                    binding.errorMessage.isVisible = true
                    binding.errorMessage.text = movieDetailsState.errorMessage

                }
            }
        }

        binding.showCastButton.setOnClickListener {

            val intent = Intent(requireContext(), CastActivity::class.java).apply {
                val id = arguments?.getString(MOVIE_ID)
                Log.d("movieId", "$id")
                putExtra(MOVIE_ID, id)
                putExtra(MOVIE_TITLE, filmTitle)
            }
            startActivity(intent)
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.posterActivityContainer, CastFragment())
//                .addToBackStack("null")
//                .setReorderingAllowed(true)
//                .commit()
        }
    }
}