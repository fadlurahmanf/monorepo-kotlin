package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.domain.interceptor.BasicTokenOpenviduInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.core_network.domain.network.CoreBaseNetwork
import okhttp3.OkHttpClient

abstract class OpenviduNetwork<T>(
    context: Context,
) : CoreBaseNetwork<T>(context, "BebasNetworkLogger") {
    override fun getBaseUrl(): String {
        return BebasShared.getOpenviduBaseUrl()
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BasicTokenOpenviduInterceptor())
    }
}