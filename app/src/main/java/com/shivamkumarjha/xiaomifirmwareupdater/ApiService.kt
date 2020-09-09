package com.shivamkumarjha.xiaomifirmwareupdater

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiService {
    @GET
    fun getStringResponse(@Url url: String?): Call<String>

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("https://dummy.base.url/")
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}