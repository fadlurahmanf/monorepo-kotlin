package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.domain.interceptor.GuestTokenInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class IdentityGuestNetwork<T>(
    context: Context,
) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.getBebasUrl()}identity-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(GuestTokenInterceptor())
    }
}