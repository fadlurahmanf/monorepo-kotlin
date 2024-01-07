package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_api.domain.authenticator.UserTokenAuthenticator
import com.fadlurahmanf.bebas_api.domain.interceptor.UserTokenInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class OrderNetwork<T>(
    context: Context,
) : BebasBaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.getBebasUrl()}order-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(UserTokenInterceptor())
            .authenticator(UserTokenAuthenticator(context))
            .addInterceptor(bodyLoggingInterceptor())
            .addInterceptor(getChuckerInterceptor())
    }
}