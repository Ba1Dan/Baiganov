package com.baiganov.devlife.models

import com.google.gson.annotations.SerializedName

class Result(
    @SerializedName("result")
    val result: List<ResultItem>,

    @SerializedName("totalCount")
    val totalCount: Int? = null
)

