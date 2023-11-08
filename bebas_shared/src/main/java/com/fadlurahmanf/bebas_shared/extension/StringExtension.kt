package com.fadlurahmanf.bebas_shared.extension

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