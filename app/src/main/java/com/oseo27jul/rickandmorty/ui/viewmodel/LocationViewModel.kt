package com.oseo27jul.rickandmorty.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.oseo27jul.rickandmorty.data.model.Locations
import com.oseo27jul.rickandmorty.domain.LocationUseCase
import kotlinx.coroutines.launch
import java.lang.Exception

class LocationViewModel:ViewModel() {
    private val locationUseCase=  LocationUseCase()
    private val _locationUse = MutableLiveData<List<Locations>>()
    val locations : LiveData<List<Locations>> = _locationUse

    private var _isLoading = MutableLiveData<Boolean>()
    private var page = 2
    val isLoading2: LiveData<Boolean> = _isLoading
    var isLastPage = false

    var isLoading = false

    private val _selectedCharacter = MutableLiveData<Locations>()
    val selectedCharacter: LiveData<Locations>
        get() = _selectedCharacter





    init {
        _isLoading.value = false
    }

    fun onCreate(page:Int){
        if(_locationUse.value == null) {
            _isLoading.postValue(true)
            //corrutina
            viewModelScope.launch {
                try {
                    val result: List<Locations> = locationUseCase.getListLocation(page)
                    result.let {
                        _locationUse.postValue(it)
                    }
                } catch (_: Exception) {

                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun loadMoreLocation(){
        if(isLoading || isLastPage)return

        isLoading = true
        //cargamos
        _isLoading.postValue(true)

        //llamamos el metodo para cargar mas
        viewModelScope.launch {
            val result : List<Locations> = locationUseCase.getListLocation(page)
            result.let {
                //tenemos que guardar la lista actual para que no se sobreescriba
                val currentList = locations.value ?: emptyList()
                val updateList = currentList + it

                _locationUse.postValue(updateList)
                page++
            }

            isLoading = false
            _isLoading.postValue(false)
            isLastPage = result.isEmpty()
        }
    }

}