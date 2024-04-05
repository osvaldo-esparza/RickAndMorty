package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.core.RetrofitHelper
import com.oseo27jul.rickandmorty.data.model.CharactersModel
import com.oseo27jul.rickandmorty.data.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterService {
    private val retrofit = RetrofitHelper.getRetrofitInstance()

    suspend fun getCharacters(page:Int): CharactersModel {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(CharactersApiClient::class.java).getCharacters(page)
            (response.body() ?: CharactersModel())
        }
    }

    suspend fun getCharacter(character:Int): Character?{
        return withContext(Dispatchers.IO){
            val response = retrofit.create(CharactersApiClient::class.java).getCharacter(character)
            response.body()
        }
    }
}