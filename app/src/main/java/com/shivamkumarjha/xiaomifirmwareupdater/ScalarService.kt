package com.shivamkumarjha.xiaomifirmwareupdater

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface ScalarService {
    @GET
    fun getStringResponse(@Url url: String?): Call<String>
}