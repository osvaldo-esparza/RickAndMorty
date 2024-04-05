package com.oseo27jul.rickandmorty.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.oseo27jul.rickandmorty.data.model.Character

import com.oseo27jul.rickandmorty.domain.CharacterUseCase

import kotlinx.coroutines.launch
import java.lang.Exception

class CharactersViewModel : ViewModel() {
    private val characterUseCase = CharacterUseCase()
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private var residentsLoaded = false



    private var _isLoading = MutableLiveData<Boolean>()
    private var page = 2
    val isLoading2: LiveData<Boolean> = _isLoading
    var isLastPage = false

    var isLoading = false

    private val _selectedCharacter = MutableLiveData<Character>()
    val selectedCharacter: LiveData<Character>
        get() = _selectedCharacter


    init {
        _isLoading.value = false
    }

    fun onCreate(page: Int) {
        _isLoading.postValue(true)
        //corrutina
        viewModelScope.launch {
            try {
                val result: List<Character> = characterUseCase.getListCharacter(page)
                result.let {
                    _characters.postValue(it)
                }
            } catch (_: Exception) {

            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMoreCharacter() {

        if (isLoading || isLastPage) return


        isLoading = true
        //cargamos
        _isLoading.postValue(true)

        //llamamos el metodo para cargar mas
        viewModelScope.launch {
            val result: List<Character> = characterUseCase.getListCharacter(page)
            result.let {
                //tenemos que guardar la lista actual para que no se sobreescriba
                val currentList = characters.value ?: emptyList()
                val updateList = currentList + it

                _characters.postValue(updateList)
                page++
            }

            isLoading = false
            _isLoading.postValue(false)
            isLastPage = result.isEmpty()
        }


    }

    fun areResidentsLoaded(): Boolean {
        return residentsLoaded
    }



    fun getCharacterByURL(urls: List<String>) {
        if (residentsLoaded) return
        if (isLoading || isLastPage) return


        isLoading = true
        _isLoading.postValue(true)

        viewModelScope.launch {
            val characters = mutableListOf<Character>()

            for (url in urls) {
                val characterID = extractCharacterIdFromUrl(url)
                val character = characterUseCase.getCharacter(characterID)
                character?.let { characters.add(it) }
            }

            val currentList = _characters.value ?: emptyList()
            val updatedList =  characters
            _characters.postValue(updatedList)

            isLoading = false
            _isLoading.postValue(false)
            isLastPage = characters.isEmpty()
        }

        residentsLoaded = true
    }

    private fun extractCharacterIdFromUrl(url: String): Int {
        // Extraer el ID del personaje de la URL. Esto puede variar según la estructura de la URL.
        // Por ejemplo, podrías usar expresiones regulares o métodos de manipulación de cadenas.
        val regex = """(\d+)""".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.value?.toIntOrNull() ?: throw IllegalArgumentException("Invalid URL format")
    }




}