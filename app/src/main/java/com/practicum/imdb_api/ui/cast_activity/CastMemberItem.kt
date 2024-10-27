package com.practicum.imdb_api.ui.cast_activity

sealed interface CastMemberItem {

    data class Director (
        val id: String,
        val name: String,
        val description: String
    ): CastMemberItem

    data class Writers (
        val id: String,
        val name: String,
        val description: String
    ): CastMemberItem

    data class Actors (
        val id: String,
        val image: String,
        val name: String,
        val asCharacter: String
    ): CastMemberItem

    class DirectorPH(): CastMemberItem
    class WritersPH: CastMemberItem
    class ActorsPH: CastMemberItem
}