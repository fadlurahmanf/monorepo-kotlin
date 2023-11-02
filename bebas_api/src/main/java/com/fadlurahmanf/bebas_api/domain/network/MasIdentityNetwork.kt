package com.fadlurahmanf.bebas_api.domain.network

import android.content.Context
import com.fadlurahmanf.bebas_shared.BebasShared

abstract class MasIdentityNetwork<T>(context: Context) : BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BebasShared.bebasUrl}identity-service/"
    }
}