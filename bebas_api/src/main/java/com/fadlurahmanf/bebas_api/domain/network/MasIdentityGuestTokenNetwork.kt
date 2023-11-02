package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class MasIdentityGuestTokenNetwork<T>(
    context: Context,
) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.bebasUrl}identity-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
//            .addInterceptor(MasIdentityGuestTokenInterceptor(mappLocalDatasource))
    }
}