package com.oseo27jul.rickandmorty.ui.viewmodel


import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.android.material.snackbar.Snackbar
import com.oseo27jul.rickandmorty.R
import com.oseo27jul.rickandmorty.data.model.Character
import com.oseo27jul.rickandmorty.data.model.CharactersModel
import com.oseo27jul.rickandmorty.domain.CharacterUseCase
import com.oseo27jul.rickandmorty.ui.fragments.Characters.CharacterDetail
import kotlinx.coroutines.launch
import java.lang.Exception

class CharactersViewModel:ViewModel() {
    private val characterUseCase = CharacterUseCase()
    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private var _isLoading = MutableLiveData<Boolean>()
    private var page = 2
    val isLoading2: LiveData<Boolean> = _isLoading
    public var isLastPage = false

    public var isLoading = false

    private val _selectedCharacter = MutableLiveData<Character>()
    val selectedCharacter: LiveData<Character>
        get() = _selectedCharacter

    private var _fragmentManager: FragmentManager? = null

    fun setFragmentManager(fragmentManager: FragmentManager) {
        _fragmentManager = fragmentManager
    }

    fun onCardClicked(character: Character) {
        _selectedCharacter.value = character
        _selectedCharacter.value?.let { character ->
            navigateToCharacterDetail(character.id)
        }
    }

    private fun navigateToCharacterDetail(characterId: Int) {

    }





    init {
        _isLoading.value = false
    }

    fun onCreate(page:Int){
        _isLoading.postValue(true)
        //corrutina
        viewModelScope.launch {
            try {
                val result : List<Character> = characterUseCase.getListCharacter(page)
                result?.let {
                    _characters.postValue(it)
                }
            }
            catch (e:Exception){

            }
            finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMoreCharacter(){
        if(isLoading || isLastPage)return

        isLoading = true
        //cargamos
        _isLoading.postValue(true)

        //llamamos el metodo para cargar mas
        viewModelScope.launch {
            val result : List<Character> = characterUseCase.getListCharacter(page)
            result?.let {
                //tenemos que guardar la lista actual para que no se sobreescriba
                val currentList = characters.value ?: emptyList()
                val updateList = currentList + it

                _characters.postValue(updateList)
                page++
            }

            isLoading = false
            _isLoading.postValue(false)
            isLastPage = result.isNullOrEmpty()
        }
    }


    /*fun navigateToCharacterDetail(fragmentManager: FragmentManager, character: Character) {
        val fragmentFactory = CharacterFragmentFactory(character)
        val navHostFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.childFragmentManager.fragmentFactory = fragmentFactory
        fragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, CharacterDetailFragment::class.java, null)
            .addToBackStack(null)
            .commit()
    }*/




}