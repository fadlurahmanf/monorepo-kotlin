package com.fadlurahmanf.core_network.domain.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.core_network.domain.interceptor.CustomLoggingInterceptor

abstract class CoreBaseNetworkProvider(var context: Context, var tagCustomLogging:String = "CoreNetworkLogger") {


    fun bodyLoggingInterceptor(): CustomLoggingInterceptor {
        return CustomLoggingInterceptor(tagCustomLogging).setLevel(CustomLoggingInterceptor.Level.BODY)
    }

    fun getChuckerInterceptor(): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        return ChuckerInterceptor.Builder(context).collector(collector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(true)
            .build()
    }
}