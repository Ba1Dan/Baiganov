package com.baiganov.devlife.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baiganov.devlife.models.ResultItem
import com.baiganov.devlife.models.Section

@Database(
    entities = [ResultItem::class, Section::class],
    version = 1,
    exportSchema = false
)
abstract class PostDatabase : RoomDatabase() {

    abstract fun postsDao(): PostsDao
}