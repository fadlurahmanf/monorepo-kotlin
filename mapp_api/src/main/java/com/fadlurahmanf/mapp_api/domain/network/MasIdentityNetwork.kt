package com.fadlurahmanf.mapp_api.domain.network

import android.content.Context
import com.fadlurahmanf.mapp_shared.MappShared
import okhttp3.OkHttpClient

abstract class MasIdentityNetwork<T>(context: Context) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${MappShared.retailGuestBaseUrl}identity-service/"
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
    }
}