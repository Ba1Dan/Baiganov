package com.baiganov.devlife.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "section_table",
        foreignKeys = [ForeignKey(
        entity = ResultItem::class, parentColumns = arrayOf("id"), childColumns = arrayOf("fk_id")
    )],
)
data class Section(
    @PrimaryKey
    @ColumnInfo(name = "fk_id")
    val fkId: Int,
    @ColumnInfo(name = "section")
    val section: String
)