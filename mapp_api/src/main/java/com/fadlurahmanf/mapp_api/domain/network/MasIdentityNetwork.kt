package com.fadlurahmanf.mapp_api.domain.network

import android.content.Context
import com.fadlurahmanf.mapp_shared.MappShared

abstract class MasIdentityNetwork<T>(context: Context) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${MappShared.masGuestBaseUrl}identity-service/"
    }
}