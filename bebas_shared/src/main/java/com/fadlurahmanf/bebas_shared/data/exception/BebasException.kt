package com.fadlurahmanf.bebas_shared.data.exception

import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.fadlurahmanf.bebas_shared.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import java.lang.Exception

open class BebasException(
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

        fun generalRC(rc: String, xRequestId: String? = null): BebasException {
            return BebasException(
                idRawTitle = R.string.oops,
                xrequestId = xRequestId,
                rawMessage = "ERROR_RC_$rc"
            )
        }

        fun fromThrowable(throwable: Throwable): BebasException {
            return if (throwable is BebasException) {
                throwable
            } else {
                BebasException(
                    idRawTitle = R.string.oops,
                    rawMessage = throwable.message,
                )
            }
        }

        fun fromException(exception: Exception): BebasException {
            return if (exception is BebasException) {
                exception
            } else {
                BebasException(rawMessage = exception.message)
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
        }

        return "-"
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

            if (r.contains("ERROR_RC_")) {
                val rc = r.split("ERROR_RC_").last()
                return context.getString(R.string.general_exception_desc, rc)
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