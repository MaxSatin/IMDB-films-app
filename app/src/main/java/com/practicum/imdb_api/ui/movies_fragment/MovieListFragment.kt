package com.practicum.imdb_api.ui.movies_fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.imdb_api.R
import com.practicum.imdb_api.core.navigation.Router
import com.practicum.imdb_api.databinding.MovieListFragmentBinding
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.presentation.movies.viewmodel.MoviesSearchViewModel
import com.practicum.imdb_api.presentation.movies.state.MoviesState

import com.practicum.imdb_api.ui.movie_info_fragment.MovieInfoFragment
import debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNE_DELAY = 300L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private val router: Router by inject()
    private val adapter = MoviesAdapter(
        object : MoviesAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: Movie) {
                onMovieClickDebounce(movie)
            }

//                    val intent = Intent(this@MovieActivity, PosterActivity::class.java)
//                    intent.putExtra("poster", movie.image)
//                    intent.putExtra("movieDetails", movie.id)
//                    startActivity(intent)

            override fun onFavoriteToggleClick(movie: Movie) {
                viewModel.toggleFavorite(movie)
            }
        }
    )
    private val handler = Handler(Looper.getMainLooper())
    private var job: Job? = null
    private var isClickAllowed = true

    private lateinit var onMovieClickDebounce: (Movie) -> Unit


//    private lateinit var queryInput: EditText
//    private lateinit var placeholderMessage: TextView
//    private lateinit var moviesList: RecyclerView
//    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    private val viewModel: MoviesSearchViewModel by viewModel()
    private var _binding: MovieListFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val confirmDialog = MaterialAlertDialogBuilder(context)
            .setTitle("Вы уверены, что хотите выйти?")
            .setNegativeButton("Нет"){ dialog, which ->
            }.setPositiveButton("Да"){ dialog, which ->
                requireActivity().finish()
            }

        val callBack = object: OnBackPressedCallback(
            true
        ){
            override fun handleOnBackPressed() {
                confirmDialog.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callBack
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieListFragmentBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onMovieClickDebounce = debounce<Movie>(CLICK_DEBOUNE_DELAY, viewLifecycleOwner.lifecycle.coroutineScope, false){ movie ->
            findNavController().navigate(
                R.id.action_movieListFragment_to_movieInfoFragment,
                MovieInfoFragment.createArgs(movie.image, movie.id)
            )
        }
        binding.recyclerMovie.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerMovie.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let {
            binding.textSearch.addTextChangedListener(it)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeToastState().observe(viewLifecycleOwner) { toast ->
            showToast(toast)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        viewLifecycleOwner.lifecycleScope.launch {
            delay(CLICK_DEBOUNE_DELAY)
            isClickAllowed = true
        }
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNE_DELAY)
//        }
        return current
    }

    fun showLoading() {
        binding.recyclerMovie.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        binding.recyclerMovie.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.placeholderMessage.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(movies: List<Movie>) {
        binding.recyclerMovie.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
            else -> showLoading()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        textWatcher.let {
            binding.textSearch.removeTextChangedListener(it)
        }

    }
}