package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_api.domain.interceptor.UserTokenInterceptor
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class ContentManagementNetwork<T>(
    context: Context,
) : BebasBaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.getBebasUrl()}content-management-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(UserTokenInterceptor())
    }
}