package com.shivamkumarjha.xiaomifirmwareupdater

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiInterface = ApiInterface.create().getPhones()
        apiInterface.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                Log.d(TAG, "onResponse")
                if (response?.body() != null) {
                    Log.d(TAG, "response not null")
                } else {
                    Log.d(TAG, "response is null")
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                Log.d(TAG, "onFailure")
            }
        })
    }
}