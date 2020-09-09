package com.shivamkumarjha.xiaomifirmwareupdater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shivamkumarjha.xiaomifirmwareupdater.ApiService.Companion.miJSON
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // view model
        val file = File(filesDir, miJSON)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.callApi(file)
    }
}