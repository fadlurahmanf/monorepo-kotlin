package com.fadlurahmanf.mapp_api.domain.network

import android.content.Context
import com.fadlurahmanf.mapp_shared.MappShared

abstract class MasIdentityNetworkMapp<T>(context: Context) : MappBaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${MappShared.retailGuestBaseUrl}identity-service/"
    }
}