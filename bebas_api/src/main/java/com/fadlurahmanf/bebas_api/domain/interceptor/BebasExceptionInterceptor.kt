package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class BebasExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code != 200) {
                when (response.code) {
                    404 -> {
                        throw BebasException.generalRC("RC_404")
                    }
                }
            }
            return response
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