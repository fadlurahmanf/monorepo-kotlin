package com.fadlurahmanf.bebas_shared.data.exception

import android.content.Context
import androidx.annotation.StringRes
import com.fadlurahmanf.bebas_shared.R
import com.google.gson.Gson
import com.google.gson.JsonObject

class TransactionException(
    override var httpStatusCode: Int? = null,
    override var xrequestId: String? = null,
    override var statusCode: String? = null,
    @StringRes override var idRawTitle: Int? = null,
    override var rawTitle: String? = null,
    override var title: String? = null,
    @StringRes override var idRawMessage: Int? = null,
    override var rawMessage: String? = null,
    override var message: String? = null,
    override var defaultMessage: String? = null,
    @StringRes override var idRawButtonText: Int? = R.string.ok,
    override var buttonText: String? = null,
) : BebasException(
    httpStatusCode = httpStatusCode,
    xrequestId = xrequestId,
    statusCode = statusCode,
    idRawTitle = idRawTitle,
    rawTitle = rawTitle,
    title = title,
    idRawMessage = idRawMessage,
    rawMessage = rawMessage,
    message = message,
    defaultMessage = defaultMessage,
    idRawButtonText = idRawButtonText,
    buttonText = buttonText
) {

    companion object {

        fun generalRC(rc: String, xRequestId: String? = null): TransactionException {
            return TransactionException(
                idRawTitle = R.string.oops,
                xrequestId = xRequestId,
                rawMessage = "ERROR_RC_$rc"
            )
        }

        fun fromThrowable(throwable: Throwable): TransactionException {
            return if (throwable is TransactionException) {
                throwable
            } else {
                TransactionException(
                    idRawTitle = R.string.oops,
                    rawMessage = throwable.message,
                )
            }
        }

        fun fromException(exception: Exception): TransactionException {
            return if (exception is TransactionException) {
                exception
            } else {
                TransactionException(rawMessage = exception.message)
            }
        }
    }

    override fun toProperTitle(context: Context): String {
        return getTitle(context, idRawTitle, rawTitle, title)
    }

    override fun toProperMessage(context: Context): String {
        return getMessage(context, idRawMessage, rawMessage, message, defaultMessage)
    }

    override fun toProperButtonText(context: Context): String {
        buttonText?.let {
            return it
        }

        idRawButtonText?.let { it ->
            return context.getString(it)
        }

        return context.getString(R.string.ok)
    }

    override fun getTitle(
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

    override fun getMessage(
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

            if (r.contains("TRANSFER_REJECTION_CODE_")) {
                val rc = r.split("TRANSFER_REJECTION_CODE_").last()
                return context.getString(R.string.general_exception_desc, rc)
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

    override fun toJson(): String? {
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