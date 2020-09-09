package com.shivamkumarjha.xiaomifirmwareupdater

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://your.base.url/")
            .build()

        val scalarService: ScalarService = retrofit.create(ScalarService::class.java)
        val stringCall: Call<String> = scalarService.getStringResponse("https://raw.githubusercontent.com/XiaomiFirmwareUpdater/miui-updates-tracker/master/data/latest.yml")
        stringCall.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(TAG, "onResponse")
                if (response.isSuccessful) {
                    Log.d(TAG, "response.isSuccessful")
                    if (response.body() != null) {
                        Log.d(TAG, "response not null")
                        Log.d(TAG, response.body().toString())
                    } else {
                        Log.d(TAG, "response is null")
                    }
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {
                Log.d(TAG, "onFailure")
            }
        })
    }
}