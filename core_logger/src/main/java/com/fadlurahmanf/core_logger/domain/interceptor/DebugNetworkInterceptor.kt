package com.fadlurahmanf.core_logger.domain.interceptor

import android.util.Log
import com.moczul.ok2curl.Configuration
import com.moczul.ok2curl.CurlCommandGenerator
import okhttp3.Interceptor
import okhttp3.Response

class DebugNetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val configuration = Configuration()
        val curl = CurlCommandGenerator(configuration).generate(request)
        Log.d("MappLogger", "CURL: $curl")
        return chain.proceed(request)
    }
}