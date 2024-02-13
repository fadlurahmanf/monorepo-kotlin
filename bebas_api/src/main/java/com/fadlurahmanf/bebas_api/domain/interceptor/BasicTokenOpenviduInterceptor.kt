package com.fadlurahmanf.bebas_api.domain.interceptor

import android.util.Base64
import android.util.Log
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class BasicTokenOpenviduInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = getRequest(chain.request())
            return chain.proceed(request)
        } catch (e: Throwable) {
            throw BebasException(
                idRawMessage = R.string.socket_exception_desc,
                idRawTitle = R.string.oops,
            )
        }
    }

    private fun getRequest(request: Request): Request {
        return request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader(
                "Authorization",
                "Basic ${
                    Base64.encodeToString(
                        BebasShared.openviduBasicHeader.toByteArray(),
                        Base64.NO_WRAP
                    )
                }"
            ).build()
    }
}