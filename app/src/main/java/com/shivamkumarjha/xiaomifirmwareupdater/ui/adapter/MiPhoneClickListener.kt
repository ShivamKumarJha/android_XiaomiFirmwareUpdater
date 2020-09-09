package com.shivamkumarjha.xiaomifirmwareupdater.ui.adapter

import com.shivamkumarjha.xiaomifirmwareupdater.model.MiPhone

interface MiPhoneClickListener {
    fun onCopyClick(miPhone: MiPhone)
    fun onDownloadClick(miPhone: MiPhone)
}
