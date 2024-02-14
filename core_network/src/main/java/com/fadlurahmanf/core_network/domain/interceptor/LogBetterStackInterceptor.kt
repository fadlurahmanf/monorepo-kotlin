package com.fadlurahmanf.core_network.domain.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LogBetterStackInterceptor(val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        println("MASUK LogBetterStackInterceptor: $token")
        return chain.proceed(logBetterStackRequest(chain))
    }

    private fun logBetterStackRequest(chain: Interceptor.Chain): Request {
        return chain.request().newBuilder().addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()
    }
}