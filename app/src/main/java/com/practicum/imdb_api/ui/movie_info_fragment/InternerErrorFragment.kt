package com.practicum.imdb_api.ui.movie_info_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.imdb_api.databinding.NetworkErrorFragmentBinding
class InternerErrorFragment: Fragment() {

    private var _binding: NetworkErrorFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NetworkErrorFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}