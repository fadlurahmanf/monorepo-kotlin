package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.data.exception.FulfillmentException
import com.fadlurahmanf.bebas_shared.data.exception.OrderException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.net.UnknownHostException

class OrderExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val xrequestId = request.headers["X-Request-ID"]
            val response = chain.proceed(request)
            if (response.code != 200) {
                val body = getResponseBody(response)
                throw OrderException.generalRC(
                    "BHSC_${response.code}",
                    xRequestId = xrequestId
                )
            }
            return response
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw OrderException(
                    idRawMessage = R.string.socket_exception_desc,
                    idRawTitle = R.string.oops,
                )
            }
            throw e
        }
    }

    private fun getResponseBody(response: Response): JSONObject {
        return try {
            val bodystring = response.peekBody(Long.MAX_VALUE).string()
            val json = JSONObject(bodystring)
            json
        } catch (e: Throwable) {
            JSONObject()
        }
    }
}