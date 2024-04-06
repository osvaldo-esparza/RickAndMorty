package com.oseo27jul.rickandmorty.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oseo27jul.rickandmorty.data.model.Episodes

import com.oseo27jul.rickandmorty.domain.EpisodesUseCase
import com.oseo27jul.rickandmorty.ui.fragments.EpisodesFragment
import kotlinx.coroutines.launch
import java.lang.Exception

class EpisodesViewModel:ViewModel() {
    private val episodeUseCase = EpisodesUseCase()

    private val _episodeuse = MutableLiveData<List<Episodes>>()
    val episodes : LiveData<List<Episodes>> = _episodeuse

    private var _isLoading = MutableLiveData<Boolean>()
    private var page = 2
    val isLoading2: LiveData<Boolean> = _isLoading
    var isLastPage = false

    var isLoading = false

    private val _selectedCharacter = MutableLiveData<Episodes>()
    val selectedCharacter: LiveData<Episodes>
        get() = _selectedCharacter

    init {
        _isLoading.value = false
    }

    fun onCreate(page:Int){
        if(_episodeuse.value == null) {
            _isLoading.postValue(true)
            //corrutina
            viewModelScope.launch {
                try {
                    val result: List<Episodes> = episodeUseCase.getEpisode(page)
                    result.let {
                        _episodeuse.postValue(it)
                    }
                } catch (ex: Exception) {
                    Log.i("osvaldo",ex.message.toString())

                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }


    fun loadMoreEpisodes(){
        if(isLoading || isLastPage)return

        isLoading = true
        //cargamos
        _isLoading.postValue(true)

        //llamamos el metodo para cargar mas
        viewModelScope.launch {
            val result : List<Episodes> = episodeUseCase.getEpisode(page)
            result.let {
                //tenemos que guardar la lista actual para que no se sobreescriba
                val currentList = episodes.value ?: emptyList()
                val updateList = currentList + it

                _episodeuse.postValue(updateList)
                page++
            }

            isLoading = false
            _isLoading.postValue(false)
            isLastPage = result.isEmpty()
        }
    }


}