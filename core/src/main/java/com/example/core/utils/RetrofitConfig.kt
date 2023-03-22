package com.example.core.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    @Suppress("SameParameterValue")
    private fun httpClientBuilder(isDebug: Boolean) = OkHttpClient.Builder().apply {
        if(isDebug)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
    }.build()

    fun getRetrofitBuilder(url: String, isDebug: Boolean): Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClientBuilder(isDebug))
            .build()
}