package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class MappExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw BebasException(
                    idRawMessage = R.string.socket_exception_desc,
                    idRawTitle = R.string.oops,
                )
            }
            throw e
        }
    }
}