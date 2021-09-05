package com.baiganov.devlife.data

import com.baiganov.devlife.data.database.PostsDao
import com.baiganov.devlife.models.ResultItem
import com.baiganov.devlife.models.Section
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val postsDao: PostsDao
) {

    suspend fun getPosts(): List<ResultItem> {
        return postsDao.getPosts()
    }

    suspend fun insertRecipes(posts: List<ResultItem>) {
        postsDao.insertPosts(posts)
    }

    suspend fun insertSection(sections: List<Section>) {
        postsDao.insertSections(sections)
    }

    suspend fun getPosts(section: String): List<ResultItem> {
        return postsDao.getPosts(section)
    }

    suspend fun deleteAll() {
        postsDao.deleteAllSections()
        postsDao.deleteAll()
    }
}