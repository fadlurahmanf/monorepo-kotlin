package com.fadlurahmanf.mapp_api.domain.network

import android.content.Context
import com.fadlurahmanf.core_network.domain.network.CoreBaseNetwork
import com.fadlurahmanf.mapp_api.domain.interceptor.MappExceptionInterceptor
import okhttp3.OkHttpClient

abstract class MappBaseNetwork<T>(context: Context) :
    CoreBaseNetwork<T>(context, "MappNetworkLogger") {

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(MappExceptionInterceptor(context))
    }
}