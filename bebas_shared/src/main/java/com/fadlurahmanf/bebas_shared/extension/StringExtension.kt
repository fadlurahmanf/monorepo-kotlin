package com.fadlurahmanf.bebas_shared.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.maskPhoneNumber(): String {
    if (startsWith("08")) {
        val newChar = replaceFirst("08", "+628")
        var newString = ""
        for (element in newChar.indices) {
            if (element in 6..9) {
                newString += "*"
            } else {
                newString += newChar[element]
            }
        }
        return newString
    } else if (startsWith("628")) {
        val newChar = replaceFirst("628", "+628")
        var newString = ""
        for (element in newChar.indices) {
            if (element in 6..9) {
                newString += "*"
            } else {
                newString += newChar[element]
            }
        }
        return newString
    } else {
        return this
    }
}


fun String.utcToLocal(): Date? {
    try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        return parser.parse(this)
    } catch (e: Throwable) {
        return null
    }
}