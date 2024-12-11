package com.practicum.imdb_api.ui.root_activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.imdb_api.R
import com.practicum.imdb_api.databinding.RootActivityBinding

class RootActivity : AppCompatActivity() {


    lateinit var binding: RootActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RootActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootContainerView) as NavHostFragment
        val navHostController = navHostFragment.findNavController()

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navHostController)

        navHostController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieInfoFragment -> binding.bottomNavigationView.isVisible = false
                R.id.castFragment -> binding.bottomNavigationView.isVisible = false
                else -> binding.bottomNavigationView.isVisible = true
            }
        }


//        viewModel = ViewModelProvider(this, MoviesSearchViewModel.getViewModelFactory())[MoviesSearchViewModel::class.java]
//        placeholderMessage = findViewById(R.id.placeholderMessage)
//        queryInput = findViewById(R.id.textSearch)
//        moviesList = findViewById(R.id.recyclerMovie)
//        progressBar = findViewById(R.id.progressBar)

//        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

//        if (savedInstanceState == null) {
//            // Добавляем фрагмент в контейнер
//            navigator.openFragment(MainFragment())
//        }


//    override fun onResume() {
//        super.onResume()
//        navigatorHolder.attachNavigator(navigator)
//    }
//
//    // Открепляем Navigator от NavigatorHolder
//    override fun onPause() {
//        super.onPause()
//        navigatorHolder.detachNavigator()
//    }
    }

}


