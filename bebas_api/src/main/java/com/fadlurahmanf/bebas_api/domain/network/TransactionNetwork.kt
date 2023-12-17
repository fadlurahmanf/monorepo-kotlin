package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_api.domain.authenticator.UserTokenAuthenticator
import com.fadlurahmanf.bebas_api.domain.interceptor.UserTokenInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class TransactionNetwork<T>(
    context: Context,
) : BebasBaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.getBebasUrl()}transaction-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(UserTokenInterceptor())
            .addInterceptor(bodyLoggingInterceptor())
            .addInterceptor(getChuckerInterceptor())
            .authenticator(UserTokenAuthenticator(context))
    }
}