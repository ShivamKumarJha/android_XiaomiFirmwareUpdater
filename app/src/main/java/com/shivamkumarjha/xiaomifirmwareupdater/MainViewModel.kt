package com.shivamkumarjha.xiaomifirmwareupdater

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

class MainViewModel : ViewModel() {
    private var gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    fun convertYamlToJson(yaml: String): String {
        val yamlReader = ObjectMapper(YAMLFactory())
        val obj: Any = yamlReader.readValue(yaml, Any::class.java)
        val jsonWriter = ObjectMapper()
        return jsonWriter.writeValueAsString(obj)
    }

    fun callApi(file: File) {
        val stringCall: Call<String> = ApiService.create().getStringResponse(ApiService.miURL)
        stringCall.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(TAG, "onResponse")
                if (response.isSuccessful) {
                    Log.d(TAG, "response.isSuccessful")
                }
                if (response.body() != null) {
                    Log.d(TAG, "response not null")
                    // yaml to json
                    Log.d(TAG, "Converting yaml to json")
                    val jsonString = convertYamlToJson(response.body().toString())
                    // json to ArrayList
                    Log.d(TAG, "Converting json to ArrayList")
                    val detailsTypeToken = object : TypeToken<ArrayList<MiPhone>>() {}.type
                    val miPhones: ArrayList<MiPhone> = gson.fromJson(jsonString, detailsTypeToken)
                    // print to json file
                    Log.d(TAG, "Storing ArrayList to file")
                    val data = gson.toJson(miPhones)
                    try {
                        val outputStreamWriter = OutputStreamWriter(file.outputStream())
                        outputStreamWriter.write(data)
                        outputStreamWriter.close()
                    } catch (e: IOException) {
                        Log.e(TAG, "File write failed: $e")
                    }
                } else {
                    Log.d(TAG, "response is null")
                }
            }

            override fun onFailure(call: Call<String?>?, t: Throwable?) {
                Log.e(TAG, "onFailure")
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}