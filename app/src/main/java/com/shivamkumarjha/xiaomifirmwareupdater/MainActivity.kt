package com.shivamkumarjha.xiaomifirmwareupdater

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val miURL =
            "https://raw.githubusercontent.com/XiaomiFirmwareUpdater/miui-updates-tracker/master/data/latest.yml"
        val stringCall: Call<String> = ApiService.create().getStringResponse(miURL)
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
                        val file = File(filesDir, "Mi.json")
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

    fun convertYamlToJson(yaml: String): String {
        val yamlReader = ObjectMapper(YAMLFactory())
        val obj: Any = yamlReader.readValue(yaml, Any::class.java)
        val jsonWriter = ObjectMapper()
        return jsonWriter.writeValueAsString(obj)
    }
}