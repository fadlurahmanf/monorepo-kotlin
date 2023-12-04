package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_api.domain.interceptor.BebasExceptionInterceptor
import com.fadlurahmanf.core_network.domain.network.CoreBaseNetwork
import okhttp3.OkHttpClient

abstract class BebasBaseNetwork<T>(context: Context):CoreBaseNetwork<T>(context, "BebasLoggerNetwork") {

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BebasExceptionInterceptor(context))
    }
}