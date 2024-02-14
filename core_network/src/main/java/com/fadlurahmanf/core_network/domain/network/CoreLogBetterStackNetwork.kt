package com.fadlurahmanf.core_network.domain.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.fadlurahmanf.core_network.domain.interceptor.LogBetterStackInterceptor
import okhttp3.OkHttpClient

abstract class CoreLogBetterStackNetwork<T>(
    context: Context,
    tag: String = "CoreLoggerNetwork"
) :
    CoreBaseNetwork<T>(context, tag) {
    override fun getBaseUrl(): String = "https://in.logs.betterstack.com"

    abstract val logBetterStackToken: String

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(LogBetterStackInterceptor(logBetterStackToken))
            .addInterceptor(bodyLoggingInterceptor())
            .addInterceptor(getChuckerInterceptor())
    }
}