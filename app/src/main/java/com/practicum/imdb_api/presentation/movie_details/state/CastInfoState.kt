package com.practicum.imdb_api.presentation.movie_details.state

import com.practicum.imdb_api.domain.models.cast_members.CastInfo

sealed interface CastInfoState {
    object Loading: CastInfoState

    data class Content (
        val castInfo : CastInfo
    ): CastInfoState

    data class Error (
        val error: String
    ): CastInfoState

    data class Empty (
        val error: String
    ): CastInfoState
}