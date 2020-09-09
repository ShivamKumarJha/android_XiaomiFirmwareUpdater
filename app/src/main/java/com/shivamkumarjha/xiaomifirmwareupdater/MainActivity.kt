package com.shivamkumarjha.xiaomifirmwareupdater

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
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
        callApi("https://raw.githubusercontent.com/RealmeUpdater/realme-updates-tracker/master/data/latest.yml")
        callApi("https://raw.githubusercontent.com/XiaomiFirmwareUpdater/miui-updates-tracker/master/data/latest.yml")
    }

    private fun callApi(url: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://dummy.base.url/")
            .build()

        val scalarService: ScalarService = retrofit.create(ScalarService::class.java)
        val stringCall: Call<String> = scalarService.getStringResponse(url)
        stringCall.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(TAG, "onResponse")
                if (response.isSuccessful) {
                    Log.d(TAG, "response.isSuccessful")
                }
                if (response.body() != null) {
                    Log.d(TAG, "response not null")
                    Log.d(TAG, convertYamlToJson(response.body().toString()))
                } else {
                    Log.d(TAG, "response is null")
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {
                Log.d(TAG, "onFailure")
            }
        })
    }

    fun convertYamlToJson(yaml: String): String {
        val yamlReader = ObjectMapper(YAMLFactory())
        val obj: Any = yamlReader.readValue(yaml, Any::class.java)
        val jsonWriter = ObjectMapper()
        return jsonWriter.writeValueAsString(obj)
    }
}