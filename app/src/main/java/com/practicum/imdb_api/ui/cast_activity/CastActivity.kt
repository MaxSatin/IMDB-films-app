package com.practicum.imdb_api.ui.cast_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.practicum.imdb_api.R

import com.practicum.imdb_api.databinding.ActivityCastBinding

class CastActivity : AppCompatActivity() {

    lateinit var binding: ActivityCastBinding

    companion object {
        private const val MOVIE_ID = "movie_id"
        private const val MOVIE_TITLE = "movie_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_container, CastFragment.newInstance(
                    intent.getStringExtra(MOVIE_ID),
                    intent.getStringExtra(MOVIE_TITLE)
                ))
            }
        }

    }
}