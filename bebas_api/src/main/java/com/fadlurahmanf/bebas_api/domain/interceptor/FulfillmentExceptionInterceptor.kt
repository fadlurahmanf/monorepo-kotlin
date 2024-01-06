package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.data.exception.FulfillmentException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.net.UnknownHostException

class FulfillmentExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val xrequestId = request.headers["X-Request-ID"]
            val response = chain.proceed(request)
            if (response.code != 200) {
                val body = getResponseBody(response)
                if (body.has("status") && body.has("code")
                ) {
                    val code = body.getString("code")
                    if (code == "U01") {
                        throw FulfillmentException(
                            rawMessage = "FULFILLMENT_ERROR_RC_$code",
                            rawTitle = "FULFILLMENT_ERROR_RC_$code"
                        )
                    } else {
                        throw FulfillmentException(
                            rawMessage = "FULFILLMENT_ERROR_RC_$code",
                            idRawTitle = R.string.oops_error_occured,
                            xrequestId = xrequestId
                        )
                    }
                } else {
                    throw FulfillmentException.generalRC(
                        "BHSC_${response.code}",
                        xRequestId = xrequestId
                    )
                }
            }
            return response
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw FulfillmentException(
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