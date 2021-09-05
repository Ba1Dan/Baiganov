package com.baiganov.devlife.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "posts_table")
data class ResultItem(
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("gifURL")
    val gifURL: String? = null,
    @SerializedName("gifSize")
    val gifSize: Int? = null,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
)