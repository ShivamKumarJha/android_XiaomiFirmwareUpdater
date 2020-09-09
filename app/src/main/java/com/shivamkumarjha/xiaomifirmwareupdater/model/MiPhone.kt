package com.shivamkumarjha.xiaomifirmwareupdater.model

import com.google.gson.annotations.SerializedName

data class MiPhone(
    @SerializedName("android") val android: Double,
    @SerializedName("branch") val branch: String,
    @SerializedName("codename") val codename: String,
    @SerializedName("date") val date: String,
    @SerializedName("link") val link: String,
    @SerializedName("md5") val md5: String,
    @SerializedName("method") val method: String,
    @SerializedName("name") val name: String,
    @SerializedName("size") val size: String,
    @SerializedName("version") val version: String
)