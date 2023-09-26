package com.fadlurahmanf.mapp_api.domain.interceptor

import android.content.Context
import com.fadlurahmanf.mapp_api.R
import com.fadlurahmanf.mapp_api.data.exception.MappException
import okhttp3.Interceptor
import okhttp3.Response
import java.lang.Exception
import java.net.UnknownHostException

class MappExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw MappException(
                    idRawMessage = R.string.socket_exception_desc,
                    idRawTitle = R.string.oops,
                )
            }
            throw e
        }
    }
}