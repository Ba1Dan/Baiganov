package com.baiganov.devlife.data.network

import com.baiganov.devlife.models.Result
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PostApi {

    @GET("/{section}/{page}")
    suspend fun getData(
        @Path("section") section: String,
        @Path("page") page: Int = 0,
    ): Response<Result>
}