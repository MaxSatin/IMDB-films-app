package com.practicum.imdb_api.presentation.persons.state

import com.practicum.imdb_api.domain.models.movie.Movie
import com.practicum.imdb_api.domain.models.person.Person
import com.practicum.imdb_api.presentation.movies.state.MoviesState

sealed interface PersonsState {
    object Loading: PersonsState

    data class Content(
        val movies: List<Person>
    ): PersonsState

    data class Error(
        val errorMessage: String
    ): PersonsState

    data class Empty(
        val message: String
    ): PersonsState
}