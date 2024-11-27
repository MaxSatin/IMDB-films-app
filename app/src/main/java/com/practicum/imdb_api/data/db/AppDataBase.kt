package com.practicum.imdb_api.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.imdb_api.data.db.dao.MovieDao
import com.practicum.imdb_api.data.db.entity.MovieEntity

@Database(version = 1, entities = [MovieEntity::class])
abstract class AppDataBase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}