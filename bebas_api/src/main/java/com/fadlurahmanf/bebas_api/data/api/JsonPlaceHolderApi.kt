package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.example.PostResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface JsonPlaceHolderApi {
    @GET("posts")
    fun getListPost(): Observable<List<PostResponse>>
}