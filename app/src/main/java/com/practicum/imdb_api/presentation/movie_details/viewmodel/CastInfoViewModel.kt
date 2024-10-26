package com.practicum.imdb_api.presentation.movie_details.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.imdb_api.domain.api.MoviesInteractor
import com.practicum.imdb_api.domain.models.cast_members.CastInfo
import com.practicum.imdb_api.presentation.movie_details.state.CastInfoState

class CastInfoViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor
): ViewModel() {

    private val castInfoLiveData = MutableLiveData<CastInfoState>()
    fun getCastInfoLiveData() : LiveData<CastInfoState> = castInfoLiveData

    init {
        getCastInfo()
    }
   fun getCastInfo(){
        renderState(
            CastInfoState.Loading
        )
        moviesInteractor.getCastInfo(
            movieId = movieId,
            consumer = object : MoviesInteractor.CastInfoConsumer {
                override fun consume(castInfo: CastInfo?, errorMessage: String?) {
                    when {
                        errorMessage != null -> renderState(CastInfoState.Error(errorMessage))
                        castInfo == null -> renderState(CastInfoState.Empty("Информация о команде отсутствует"))
                        else -> {
                            renderState(CastInfoState.Content(castInfo))
                        }
                    }
                }

            }
        )
    }

    private fun renderState(state: CastInfoState){
        castInfoLiveData.postValue(state)
    }
}