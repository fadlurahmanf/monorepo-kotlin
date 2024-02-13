package com.fadlurahmanf.bebas_api.domain.interceptor

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.R
import com.fadlurahmanf.bebas_shared.data.exception.TransactionException
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.net.UnknownHostException

class TransactionExceptionInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val xrequestId = request.headers["X-Request-ID"]
            val response = chain.proceed(request)
            if (response.code != 200) {
                val body = getResponseBody(response)
                if (body.has("message") && body.optString("message")
                        .startsWith("TRANSFER_REJECTION_CODE_")
                ) {
                    throw TransactionException(
                        rawMessage = body.getString("message"),
                        idRawTitle = R.string.oops_error_occured,
                        xrequestId = xrequestId
                    )
                } else {
                    throw TransactionException.generalRC(
                        "BHSC_${response.code}",
                        xRequestId = xrequestId
                    )
                }
            }
            return response
        } catch (e: Throwable) {
            if (e is UnknownHostException) {
                throw TransactionException(
                    idRawMessage = R.string.socket_exception_desc,
                    idRawTitle = R.string.oops,
                )
            }
            throw e
        }
    }

    private fun getResponseBody(response: Response): JSONObject {
        try {
            val bodystring = response.peekBody(Long.MAX_VALUE).string()
            val json = JSONObject(bodystring)
            return json
        } catch (e: Throwable) {
            Log.e("BebasLogger", "ERROR: ${e.message}")
            return JSONObject()
        }
    }
}