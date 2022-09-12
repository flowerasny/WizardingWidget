package com.flowerasny.widget.dogs

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ElixirsRepository {

    private val elixirsApi = Retrofit.Builder()
        .baseUrl("https://wizard-world-api.herokuapp.com")
        .callFactory(OkHttpClient())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(
                    KotlinJsonAdapterFactory()
                ).build()
            )
        )
        .build()
        .create(ElixirsApi::class.java)

    suspend fun getElixirs() = elixirsApi.getElixirs().shuffled()
}
