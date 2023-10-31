package com.fadlurahmanf.mapp_api.data.api

import com.fadlurahmanf.mapp_api.data.dto.example.PostResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.POST

interface JsonPlaceHolderApi {
    @GET("posts")
    fun getListPost(): Observable<List<PostResponse>>
}