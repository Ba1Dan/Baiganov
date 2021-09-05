package com.baiganov.devlife.data.database

import androidx.room.*
import com.baiganov.devlife.models.ResultItem
import com.baiganov.devlife.models.Section

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ResultItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ResultItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(posts: List<Section>)

    @Query("SELECT * FROM posts_table ORDER BY id DESC")
    suspend fun getPosts(): List<ResultItem>

    @Query("SELECT * FROM posts_table AS p JOIN section_table AS s ON p.id = s.fk_id WHERE s.section = :section ORDER BY id DESC")
    suspend fun getPosts(section: String): List<ResultItem>

    @Delete
    suspend fun deletePost(post: ResultItem)

    @Query("DELETE FROM posts_table")
    suspend fun deleteAll()

    @Query("DELETE FROM section_table")
    suspend fun deleteAllSections()
}