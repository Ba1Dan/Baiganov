package com.baiganov.devlife.data.database

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class TopResultItem(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("videoSize")
    val videoSize: Int? = null,
    @SerializedName("gifURL")
    val gifURL: String? = null,
    @SerializedName("gifSize")
    val gifSize: Int? = null,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
)