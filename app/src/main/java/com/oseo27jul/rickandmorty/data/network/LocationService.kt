package com.oseo27jul.rickandmorty.data.network

import com.oseo27jul.rickandmorty.core.RetrofitHelper
import com.oseo27jul.rickandmorty.data.model.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LocationService {
    private val retrofit = RetrofitHelper.getRetrofitInstance()

    suspend fun getLocations(page:Int) : LocationResponse {
        return withContext(Dispatchers.IO){
            val response = retrofit.create(LocationApiClient::class.java).getLocation(page)
            (response.body() ?: LocationResponse())
        }
    }
}