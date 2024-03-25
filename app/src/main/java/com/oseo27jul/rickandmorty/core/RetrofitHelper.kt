package com.oseo27jul.rickandmorty.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    // Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Función para obtener la instancia de Retrofit
    fun getRetrofitInstance(): Retrofit {
        return retrofit
    }

    // Función para obtener una instancia del servicio deseado
    private inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
