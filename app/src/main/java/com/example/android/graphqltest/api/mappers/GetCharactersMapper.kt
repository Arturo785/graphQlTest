package com.example.android.graphqltest.api.mappers

import com.example.android.graphqltest.api.models.Character
import com.example.graphqlapollo.CharactersListQuery

fun CharactersListQuery.Data.toCharacters(): List<Character>? {
    return this.characters?.results?.map {
        it?.toCharacter() ?: Character("","","")
    }
}

fun CharactersListQuery.Result.toCharacter(): Character {
    return Character(
        this.id ?: "",
        this.name ?: "",
        this.species ?: ""
    )
}