package com.oseo27jul.rickandmorty.data

import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.CharacterProvider
import com.oseo27jul.rickandmorty.data.model.CharactersModel
import com.oseo27jul.rickandmorty.data.network.CharacterService

class CharacterRepository {
    private val api = CharacterService()

    suspend fun getCharacter(page:Int): CharactersModel {
        val response = api.getCharacters(page)
        CharacterProvider.character = response
        return response
    }

    suspend fun getListCharecter(page: Int): List<Character>{
        val response = api.getCharacters(page).results
        return response
    }
}