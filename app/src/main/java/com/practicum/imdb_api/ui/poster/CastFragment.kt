package com.practicum.imdb_api.ui.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.imdb_api.databinding.CastFragmentBinding

class CastFragment : Fragment() {

    private var _binding: CastFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = CastFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}

