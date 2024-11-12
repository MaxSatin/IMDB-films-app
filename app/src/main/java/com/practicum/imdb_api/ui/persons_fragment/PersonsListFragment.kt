package com.practicum.imdb_api.ui.persons_fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.imdb_api.databinding.PersonsFragmentBinding
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.presentation.persons.state.PersonsState
import com.practicum.imdb_api.presentation.persons.viewmodel.PersonsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
class PersonsListFragment: Fragment() {

    companion object {
        private const val CLICK_DEBOUNE_DELAY = 1_000L
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

//    private val router: Router by inject()
    private val adapter = PersonsAdapter()

//        MoviesAdapter(
//        object : MoviesAdapter.OnMovieClickListener {
//            override fun onMovieClick(movie: Movie) {
//                if (clickDebounce()) {
//                    router.openFragment(MovieInfoFragment.newInstance(movie.image, movie.id))
//
//                }
//            }
//
////                    val intent = Intent(this@MovieActivity, PosterActivity::class.java)
////                    intent.putExtra("poster", movie.image)
////                    intent.putExtra("movieDetails", movie.id)
////                    startActivity(intent)
//
//            override fun onFavoriteToggleClick(movie: Movie) {
//                viewModel.toggleFavorite(movie)
//            }
//        }
//    )
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true


//    private lateinit var queryInput: EditText
//    private lateinit var placeholderMessage: TextView
//    private lateinit var moviesList: RecyclerView
//    private lateinit var progressBar: ProgressBar

    private var textWatcher: TextWatcher? = null

    val viewModel: PersonsViewModel by viewModel()

    private var _binding: PersonsFragmentBinding? = null
    private val binding: PersonsFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PersonsFragmentBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerPersons.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerPersons.adapter = adapter

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNE_DELAY)
        }
        return current
    }

    fun showLoading() {
        binding.recyclerPersons.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        binding.recyclerPersons.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.placeholderMessage.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(persons: List<Person>) {
        binding.recyclerPersons.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.personList.clear()
        adapter.personList.addAll(persons)
        adapter.notifyDataSetChanged()
    }

    private fun render(state: PersonsState) {
        when (state) {
            is PersonsState.Loading -> showLoading()
            is PersonsState.Content -> showContent(state.movies)
            is PersonsState.Error -> showError(state.errorMessage)
            is PersonsState.Empty -> showEmpty(state.message)
            else -> showLoading()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}