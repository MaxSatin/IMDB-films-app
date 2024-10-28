package com.practicum.imdb_api.core.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface Navigator {

    val fragmentContainerViewId: Int
    val fragmentManager: FragmentManager

    fun openFragment(fragment: Fragment)
}