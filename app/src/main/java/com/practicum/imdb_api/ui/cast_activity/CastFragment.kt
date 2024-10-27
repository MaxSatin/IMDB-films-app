package com.practicum.imdb_api.ui.cast_activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.imdb_api.databinding.CastFragmentBinding
import com.practicum.imdb_api.presentation.movie_details.state.CastInfoState
import com.practicum.imdb_api.presentation.movie_details.viewmodel.CastInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CastFragment : Fragment() {

    private var _binding: CastFragmentBinding? = null
    private val binding get() = _binding!!

//    lateinit var adapter: CastActivityAdapter
    private val viewModel: CastInfoViewModel by viewModel() {
        Log.d("parameters", "${arguments?.getString(MOVIE_ID)}")
        parametersOf(arguments?.getString(MOVIE_ID))
    }

    companion object {
        private const val MOVIE_ID = "movie_id"
        private const val MOVIE_TITLE = "movie_title"
        fun newInstance(filmId: String?, filmTitle: String?): CastFragment {
            return CastFragment().apply {
                arguments = bundleOf(
                    MOVIE_ID to filmId,
                    MOVIE_TITLE to filmTitle
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = CastFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filmTitle.text = arguments?.getString(MOVIE_TITLE)


        viewModel.getCastInfoLiveData().observe(viewLifecycleOwner) { castInfo ->
            if (castInfo is CastInfoState.Content) {
                val adapter = CastActivityAdapter(castInfo.castInfo)
                Log.d("adapter", "$adapter")
                binding.castRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter.notifyDataSetChanged()
                binding.castRV.adapter = adapter
            }


        }
    }
}

