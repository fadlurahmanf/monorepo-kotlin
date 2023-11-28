package com.fadlurahmanf.mapp_api.data.datasources

import android.content.Context
import com.fadlurahmanf.mapp_api.data.api.JsonPlaceHolderApi
import com.fadlurahmanf.mapp_api.domain.network.MappBaseNetwork
import javax.inject.Inject

class JsonPlaceHolderRemoteDatasource @Inject constructor(context: Context) :
    MappBaseNetwork<JsonPlaceHolderApi>(context) {
    override fun getApi(): Class<JsonPlaceHolderApi> = JsonPlaceHolderApi::class.java

    override fun getBaseUrl(): String = "https://jsonplaceholder.typicode.com/"

    fun getListPost() = networkService(30).getListPost()
}