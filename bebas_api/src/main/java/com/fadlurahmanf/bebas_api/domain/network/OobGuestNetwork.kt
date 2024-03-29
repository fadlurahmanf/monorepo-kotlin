package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_api.domain.interceptor.BebasExceptionInterceptor
import com.fadlurahmanf.bebas_api.domain.interceptor.GuestTokenInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class OobGuestNetwork<T>(
    context: Context,
) : BebasBaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.getBebasUrl()}online-onboarding-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BebasExceptionInterceptor(context))
            .addInterceptor(GuestTokenInterceptor())
    }
}