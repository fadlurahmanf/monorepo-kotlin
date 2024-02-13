package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.JsonPlaceHolderApi
import com.fadlurahmanf.bebas_api.domain.network.BebasBaseNetwork
import javax.inject.Inject

class JsonPlaceHolderRemoteDatasource @Inject constructor(context: Context) :
    BebasBaseNetwork<JsonPlaceHolderApi>(context) {
    override fun getApi(): Class<JsonPlaceHolderApi> = JsonPlaceHolderApi::class.java

    override fun getBaseUrl(): String = "https://jsonplaceholder.typicode.com/"

    fun getListPost() = networkService().getListPost()
}