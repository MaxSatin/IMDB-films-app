package com.practicum.imdb_api.ui.history_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.imdb_api.presentation.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.imdb_api.databinding.HistoryFragmentBinding
import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.presentation.history.HistoryState

class HistoryFragment: Fragment() {

    private val viewModel by viewModel<HistoryViewModel>()
    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!

    private var adapter: HistoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter()
        binding.historyList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.historyList.adapter = adapter

        viewModel.fillData()

        viewModel.observeState().observe(viewLifecycleOwner) { historyState ->
            render(historyState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding.historyList.adapter = null
        _binding = null
    }

    private fun render (state: HistoryState){
        when (state) {
            is HistoryState.Loading -> showLoading()
            is HistoryState.Content -> showContent(state.movies)
            is HistoryState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.historyList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showContent(movies: List<Movie>){
        binding.historyList.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter?.movieHistoryList?.clear()
        adapter?.movieHistoryList?.addAll(movies)
        adapter?.notifyDataSetChanged()
    }

    private fun showEmpty(message: String){
        binding.historyList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.placeholderMessage.text = message
    }
}