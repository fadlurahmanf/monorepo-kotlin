package com.fadlurahmanf.bebas_api.domain.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.UUID

class BebasDefaultInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(getRequest(chain.request()))
    }

    private fun getRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader("X-Request-ID", UUID.randomUUID().toString())
            .build()
    }
}