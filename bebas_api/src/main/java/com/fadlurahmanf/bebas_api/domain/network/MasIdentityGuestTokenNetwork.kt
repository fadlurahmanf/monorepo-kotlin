package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.OkHttpClient

abstract class MasIdentityGuestTokenNetwork<T>(
    context: Context,
) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        Log.d("BebasLogger", "getBaseUrl: MasIdentityGuestTokenNetwork")
        return "${BebasShared.getBebasUrl()}identity-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
//            .addInterceptor(MasIdentityGuestTokenInterceptor(mappLocalDatasource))
    }
}