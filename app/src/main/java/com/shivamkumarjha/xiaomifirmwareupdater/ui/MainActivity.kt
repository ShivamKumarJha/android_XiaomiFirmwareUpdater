package com.shivamkumarjha.xiaomifirmwareupdater.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shivamkumarjha.xiaomifirmwareupdater.R
import com.shivamkumarjha.xiaomifirmwareupdater.api.ApiService.Companion.miJSON
import com.shivamkumarjha.xiaomifirmwareupdater.model.MiPhone
import com.shivamkumarjha.xiaomifirmwareupdater.ui.adapter.MiPhoneAdapter
import com.shivamkumarjha.xiaomifirmwareupdater.ui.adapter.MiPhoneClickListener
import com.shivamkumarjha.xiaomifirmwareupdater.viewmodel.MainViewModel
import java.io.File

class MainActivity : AppCompatActivity(), MiPhoneClickListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var miPhoneAdapter: MiPhoneAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // search view
        searchView = findViewById(R.id.mi_search_view_id)
        val searchIcon = searchView.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.WHITE)
        val cancelIcon = searchView.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.WHITE)
        val searchTextView = searchView.findViewById<TextView>(R.id.search_src_text)
        searchTextView.setTextColor(Color.WHITE)
        searchTextView.hint = resources.getString(R.string.search_phones)
        searchTextView.setHintTextColor(ContextCompat.getColor(this, R.color.gray_300))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                miPhoneAdapter.filter.filter(newText)
                return false
            }
        })
        // recyclerView
        recyclerView = findViewById(R.id.mi_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        miPhoneAdapter = MiPhoneAdapter(this)
        recyclerView.adapter = miPhoneAdapter
        // view model
        val file = File(filesDir, miJSON)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.callApi(file)
        mainViewModel.getPhones.observe(this, Observer {
            miPhoneAdapter.setMiPhones(it)
        })
    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("ROM_LINK", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, resources.getString(R.string.copied), Toast.LENGTH_SHORT)
            .show()
    }

    private fun openBrowser(url: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }

    private fun downloadDialog(url: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.download))
        builder.setMessage(resources.getString(R.string.download_confirm))
        builder.setPositiveButton(R.string.yes) { _, _ ->
            openBrowser(url)
        }
        builder.setNegativeButton(R.string.no) { _, _ -> }
        builder.show()
    }

    override fun onCopyClick(miPhone: MiPhone) {
        copyTextToClipboard(miPhone.link)
    }

    override fun onDownloadClick(miPhone: MiPhone) {
        downloadDialog(miPhone.link)
    }
}