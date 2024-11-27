package com.practicum.imdb_api.data.converters

import com.practicum.imdb_api.data.db.entity.MovieEntity
import com.practicum.imdb_api.data.dto.MovieDto
import com.practicum.imdb_api.domain.models.movie.Movie

class MovieDbConvertor {

    fun map(movie: MovieDto): MovieEntity{
        return MovieEntity(movie.id, movie.resultType, movie.image, movie.title, movie.description)
    }

    fun map(movie: MovieEntity): Movie {
        return Movie(movie.id, movie.resultType, movie.image, movie.title, movie.description, false)
    }
}