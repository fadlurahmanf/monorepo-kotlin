package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class IdentityExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var xrequestId: String? = null
        try {
            val request = chain.request()
            xrequestId = request.header("X-Request-ID")
            val response = chain.proceed(request)
            if (response.code != 200) {
                when (response.code) {
                    404 -> {
                        throw BebasException.generalRC("BEBAS_404", xrequestId)
                    }

                    409 -> {
                        throw BebasException.generalRC("BEBAS_409", xrequestId)
                    }
                }
            }
            return response
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw BebasException(
                    idRawMessage = R.string.socket_exception_desc,
                    xrequestId = xrequestId,
                    idRawTitle = R.string.oops,
                )
            }
            throw e
        }
    }
}