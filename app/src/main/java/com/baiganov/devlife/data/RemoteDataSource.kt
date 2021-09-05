package com.baiganov.devlife.data

import com.baiganov.devlife.data.network.PostApi
import com.baiganov.devlife.models.Result
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val postApi: PostApi
) {

    suspend fun getRecipes(section: String, page: Int): Response<Result> {
        return postApi.getData(section, page)
    }
}