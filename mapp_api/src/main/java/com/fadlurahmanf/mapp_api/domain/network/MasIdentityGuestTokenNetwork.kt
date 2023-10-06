package com.fadlurahmanf.mapp_api.domain.network

import android.content.Context
import com.fadlurahmanf.mapp_api.domain.interceptor.MasIdentityGuestTokenInterceptor
import com.fadlurahmanf.mapp_shared.MappShared
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import okhttp3.OkHttpClient

abstract class MasIdentityGuestTokenNetwork<T>(
    context: Context,
    private val mappLocalDatasource: MappLocalDatasource
) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${MappShared.masGuestBaseUrl}identity-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(MasIdentityGuestTokenInterceptor(mappLocalDatasource))
    }
}