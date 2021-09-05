package com.baiganov.devlife.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.baiganov.devlife.data.database.PostDatabase
import com.baiganov.devlife.data.database.PostsDao
import com.baiganov.devlife.models.ResultItem
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class PostsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PostDatabase
    private lateinit var dao: PostsDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PostDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.postsDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertDb() = runBlockingTest {
        val item = ResultItem(
            date = "name",
            author = "author",
            description = "description",
            gifURL = "url",
            gifSize = 1,
            id = 1
        )
        dao.insertPost(item)
        val allItems = dao.getPosts()
        dao.deletePost(item)
        assertThat(allItems).contains(item)
    }

    @Test
    fun deleteDb() = runBlockingTest {
        val item = ResultItem(
            date = "name",
            author = "author",
            description = "description",
            gifURL = "url",
            gifSize = 1,
            id = 1
        )
        dao.insertPost(item)
        dao.deletePost(item)

        val allItems = dao.getPosts()

        assertThat(allItems).doesNotContain(item)
    }
}