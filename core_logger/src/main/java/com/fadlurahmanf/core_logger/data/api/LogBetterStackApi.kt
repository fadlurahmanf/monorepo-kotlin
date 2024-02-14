package com.fadlurahmanf.core_logger.data.api

import com.fadlurahmanf.core_logger.data.request.LogBetterStackRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LogBetterStackApi {
    @POST("/")
    fun log(
        @Body request: LogBetterStackRequest
    ): Call<Response<Any>>
}