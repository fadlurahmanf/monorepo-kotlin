package com.fadlurahmanf.bebas_shared.data.exception

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_shared.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException

open class FulfillmentException(
    open var httpStatusCode: Int? = null,
    open var xrequestId: String? = null,
    open var statusCode: String? = null,
    @DrawableRes open var failedImageDrawable: Int? = null,
    @StringRes open var idRawTitle: Int? = null,
    open var rawTitle: String? = null,
    open var title: String? = null,
    @StringRes open var idRawMessage: Int? = null,
    open var rawMessage: String? = null,
    override var message: String? = null,
    open var defaultMessage: String? = null,
    @StringRes open var idRawButtonText: Int? = R.string.ok,
    open var buttonText: String? = null,
) : IOException(rawMessage) {

    companion object {

        fun generalRC(rc: String, xRequestId: String? = null): FulfillmentException {
            return FulfillmentException(
                idRawTitle = R.string.oops,
                xrequestId = xRequestId,
                rawMessage = "FULFILLMENT_ERROR_RC_$rc"
            )
        }

        fun fromThrowable(throwable: Throwable): FulfillmentException {
            return if (throwable is FulfillmentException) {
                throwable
            } else {
                FulfillmentException(
                    idRawTitle = R.string.oops,
                    rawMessage = throwable.message,
                )
            }
        }

        fun fromException(exception: Exception): FulfillmentException {
            return if (exception is FulfillmentException) {
                exception
            } else {
                FulfillmentException(rawMessage = exception.message)
            }
        }
    }

    open fun toProperTitle(context: Context): String {
        return getTitle(context, idRawTitle, rawTitle, title)
    }

    open fun toProperMessage(context: Context): String {
        return getMessage(context, idRawMessage, rawMessage, message, defaultMessage)
    }

    open fun toProperButtonText(context: Context): String {
        buttonText?.let {
            return it
        }

        idRawButtonText?.let { it ->
            return context.getString(it)
        }

        return context.getString(R.string.ok)
    }

    open fun getTitle(
        context: Context,
        @StringRes idRaw: Int?,
        raw: String?,
        main: String?,
    ): String {
        main?.let {
            return it
        }

        idRaw?.let {
            return context.getString(it)
        }

        Log.d("BebasLogger", "Fulfillment TITLE: $raw")
        raw?.let { r ->
            val identifierLowercase = context.resources.getIdentifier(
                r.lowercase(),
                "string",
                context.packageName
            )

            if (identifierLowercase != 0) {
                return context.getString(identifierLowercase)
            }

            val identifierUppercase = context.resources.getIdentifier(
                r.uppercase(),
                "string",
                context.packageName
            )

            if (identifierUppercase != 0) {
                return context.getString(identifierUppercase)
            }

            if (r.contains("FULFILLMENT_ERROR_RC_")) {
                val rc = r.split("FULFILLMENT_ERROR_RC_").last()
                return if (rc == "U01") {
                    context.getString(R.string.bill_already_paid)
                } else {
                    context.getString(R.string.general_exception_desc, rc)
                }
            }
        }

        return "-"
    }

    open fun getDrawableImage(context: Context): Drawable? {
        rawMessage?.let { r ->
            if (r.contains("FULFILLMENT_ERROR_RC_")) {
                val rc = r.split("FULFILLMENT_ERROR_RC_").last()
                return if (rc == "U01") {
                    ContextCompat.getDrawable(context, R.drawable.il_bill_already_paid)
                } else {
                    null
                }
            }
        }
        return null
    }

    open fun getMessage(
        context: Context,
        @StringRes idRaw: Int?,
        raw: String?,
        main: String?,
        default: String?
    ): String {
        main?.let {
            return it
        }

        idRaw?.let {
            return context.getString(it)
        }

        raw?.let { r ->
            val identifierLowercase = context.resources.getIdentifier(
                r.lowercase(),
                "string",
                context.packageName
            )

            if (identifierLowercase != 0) {
                return context.getString(identifierLowercase)
            }

            val identifierUppercase = context.resources.getIdentifier(
                r.uppercase(),
                "string",
                context.packageName
            )

            if (identifierUppercase != 0) {
                return context.getString(identifierUppercase)
            }

            if (r.contains("FULFILLMENT_ERROR_RC_")) {
                val rc = r.split("FULFILLMENT_ERROR_RC_").last()
                return if (rc == "U01") {
                    context.getString(R.string.bill_already_paid_desc)
                } else {
                    context.getString(R.string.general_exception_desc, rc)
                }
            }

            return context.getString(R.string.general_exception_desc_wo_param)
        }

        default?.let {
            return it
        }

        return context.getString(R.string.general_exception_desc_wo_param)
    }

    open fun toJson(): String? {
        val json = JsonObject()
        json.addProperty("httpStatusCode", httpStatusCode)
        json.addProperty("xrequestId", xrequestId)
        json.addProperty("statusCode", statusCode)
        json.addProperty("idRawTitle", idRawTitle)
        json.addProperty("rawTitle", rawTitle)
        json.addProperty("title", title)
        json.addProperty("idRawMessage", idRawMessage)
        json.addProperty("rawMessage", rawMessage)
        json.addProperty("message", message)
        json.addProperty("defaultMessage", defaultMessage)
        json.addProperty("idRawButtonText", idRawButtonText)
        json.addProperty("buttonText", buttonText)
        return Gson().toJson(json)
    }
}