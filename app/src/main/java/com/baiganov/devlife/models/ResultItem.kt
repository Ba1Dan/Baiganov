package com.baiganov.devlife.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baiganov.devlife.util.Constants.Companion.POSTS_TABLE
import com.google.gson.annotations.SerializedName


@Entity(tableName = POSTS_TABLE)
data class ResultItem(
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String? = null,
    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author: String? = null,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String? = null,
    @ColumnInfo(name = "gifURL")
    @SerializedName("gifURL")
    val gifURL: String? = null,
    @ColumnInfo(name = "gifSize")
    @SerializedName("gifSize")
    val gifSize: Int? = null,
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,
)