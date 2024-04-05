package com.oseo27jul.rickandmorty.domain

import com.oseo27jul.rickandmorty.data.CharacterRepository
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.CharactersModel

class CharacterUseCase {
    private val repository=CharacterRepository()

    suspend operator fun invoke(page:Int): CharactersModel?=repository.getCharacter(page)

    suspend fun getListCharacter(page: Int) : List<Character> = repository.getListCharecter(page)

    suspend fun getCharacter(characterID:Int): Character? = repository.getCharacterByID(characterID)

}