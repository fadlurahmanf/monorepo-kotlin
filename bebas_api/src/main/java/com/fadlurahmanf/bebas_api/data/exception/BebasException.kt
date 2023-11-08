package com.fadlurahmanf.bebas_api.data.exception

import android.content.Context
import androidx.annotation.StringRes
import com.fadlurahmanf.bebas_api.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import java.lang.Exception

class BebasException(
    var httpStatusCode: Int? = null,
    var statusCode: String? = null,
    @StringRes var idRawTitle: Int? = null,
    var rawTitle: String? = null,
    var title: String? = null,
    @StringRes var idRawMessage: Int? = null,
    var rawMessage: String? = null,
    override var message: String? = null,
    var defaultMessage: String? = null,
    var additionalData: HashMap<String, Any>? = null,
    @StringRes var idRawButtonText: Int? = R.string.ok,
    var buttonText: String? = null,
) : IOException(rawMessage) {

    companion object {

        fun generalRC(rc: String): BebasException {
            return BebasException(
                idRawTitle = R.string.oops,
                rawMessage = "ERROR_RC_$rc"
            )
        }

        fun fromThrowable(throwable: Throwable): BebasException {
            return if (throwable is BebasException) {
                throwable
            } else {
                BebasException(rawMessage = throwable.message)
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

    fun toProperTitle(context: Context): String {
        return getTitle(context, idRawTitle, rawTitle, title)
    }

    fun toProperMessage(context: Context): String {
        return getMessage(context, idRawMessage, rawMessage, message, defaultMessage)
    }

    fun toProperButtonText(context: Context): String {
        buttonText?.let {
            return it
        }

        idRawButtonText?.let { it ->
            return context.getString(it)
        }

        return context.getString(R.string.ok)
    }

    private fun getTitle(
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

    private fun getMessage(
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

            return context.getString(R.string.general_exception_desc, r)
        }

        default?.let {
            return it
        }

        return "-"
    }

    fun toJson(): String? {
        val json = JsonObject()
        json.addProperty("httpStatusCode", httpStatusCode)
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