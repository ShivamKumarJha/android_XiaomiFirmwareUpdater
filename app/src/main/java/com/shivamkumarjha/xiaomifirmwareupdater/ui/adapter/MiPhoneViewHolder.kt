package com.shivamkumarjha.xiaomifirmwareupdater.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shivamkumarjha.xiaomifirmwareupdater.R
import com.shivamkumarjha.xiaomifirmwareupdater.model.MiPhone

class MiPhoneViewHolder(
    itemView: View,
    private val clickListenerMi: MiPhoneClickListener
) : RecyclerView.ViewHolder(itemView) {
    private val phoneName: TextView = itemView.findViewById(R.id.mi_name_text_view)
    private val phoneCodeName: TextView = itemView.findViewById(R.id.mi_rom_codename_text_view)
    private val phoneAndroidVersion: TextView = itemView.findViewById(R.id.mi_rom_android_text_view)
    private val phoneROMVersion: TextView = itemView.findViewById(R.id.mi_rom_version_text_view)
    private val phoneROMSize: TextView = itemView.findViewById(R.id.mi_rom_size_text_view)
    private val phoneROMBranch: TextView = itemView.findViewById(R.id.mi_rom_branch_text_view)
    private val phoneROMDate: TextView = itemView.findViewById(R.id.mi_rom_date_text_view)
    private val phoneROMMD5: TextView = itemView.findViewById(R.id.mi_rom_md5_text_view)
    private val phoneROMLink: Button = itemView.findViewById(R.id.mi_rom_link_text_view)
    private val copyImageButton: ImageButton = itemView.findViewById(R.id.mi_copy_link_id)
    private lateinit var miPhone: MiPhone

    init {
        copyImageButton.setOnClickListener {
            clickListenerMi.onCopyClick(miPhone)
        }
        phoneROMLink.setOnClickListener {
            clickListenerMi.onDownloadClick(miPhone)
        }
    }

    @SuppressLint("SetTextI18n")
    fun initialize(miPhone: MiPhone) {
        this.miPhone = miPhone
        phoneName.text = miPhone.name
        phoneCodeName.text = "Codename: ${miPhone.codename}"
        phoneAndroidVersion.text = "Android ${miPhone.android}"
        phoneROMVersion.text = "MIUI ${miPhone.version}"
        phoneROMSize.text = "Size: ${miPhone.size}"
        phoneROMBranch.text = miPhone.branch
        phoneROMDate.text = "Date: ${miPhone.date}"
        phoneROMMD5.text = "MD5: ${miPhone.md5}"
    }
}
