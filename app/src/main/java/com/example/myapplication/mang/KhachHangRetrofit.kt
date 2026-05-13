package com.example.myapplication.mang

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KhachHangRetrofit {
    private const val BASE_URL = "http://10.0.2.2/shop_quan_ao/"

    private val logger = HttpLoggingInterceptor().apply { 
        level = HttpLoggingInterceptor.Level.BODY 
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val layGiaoDienApi: GiaoDienApi by lazy {
        retrofit.create(GiaoDienApi::class.java)
    }
}
