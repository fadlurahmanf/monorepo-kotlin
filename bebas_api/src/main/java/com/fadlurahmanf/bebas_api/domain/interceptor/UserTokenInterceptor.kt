package com.fadlurahmanf.bebas_api.domain.interceptor

import com.fadlurahmanf.bebas_shared.BebasShared
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class UserTokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(getRequest(chain.request()))
    }

    private fun getRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader("Authorization", "Bearer ${BebasShared.getAccessToken()}")
            .build()
    }
}