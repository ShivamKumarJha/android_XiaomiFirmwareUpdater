package com.shivamkumarjha.xiaomifirmwareupdater.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.shivamkumarjha.xiaomifirmwareupdater.api.ApiService
import com.shivamkumarjha.xiaomifirmwareupdater.model.MiPhone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class MainViewModel : ViewModel() {
    private var gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val _getPhones = MutableLiveData<ArrayList<MiPhone>>()
    val getPhones: LiveData<ArrayList<MiPhone>> = _getPhones

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.postValue(true)
    }

    fun convertYamlToJson(yaml: String): String {
        val yamlReader = ObjectMapper(YAMLFactory())
        val obj: Any = yamlReader.readValue(yaml, Any::class.java)
        val jsonWriter = ObjectMapper()
        return jsonWriter.writeValueAsString(obj)
    }

    private fun readFromFile(file: File): String? {
        var jsonString: String? = null
        try {
            val inputStream: FileInputStream = file.inputStream()
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            jsonString = bufferedReader.use { it.readText() }
            inputStream.close()
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "File not found: $e")
        } catch (e: IOException) {
            Log.e(TAG, "Can not read file: $e")
        }
        return jsonString
    }

    fun callApi(file: File) {
        // load from file initially if it exists
        if (file.exists()) {
            val jsonString = readFromFile(file)
            val detailsTypeToken = object : TypeToken<ArrayList<MiPhone>>() {}.type
            val phones: ArrayList<MiPhone> = gson.fromJson(jsonString, detailsTypeToken)
            _getPhones.postValue(phones)
        }
        // call api
        _isLoading.postValue(true)
        val stringCall: Call<String> = ApiService.create().getStringResponse(ApiService.miURL)
        stringCall.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.d(TAG, "onResponse")
                _isLoading.postValue(false)
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
                    _getPhones.postValue(miPhones)
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
                _isLoading.postValue(false)
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}