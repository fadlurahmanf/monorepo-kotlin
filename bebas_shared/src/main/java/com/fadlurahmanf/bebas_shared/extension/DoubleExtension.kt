package com.fadlurahmanf.bebas_shared.extension

import java.text.NumberFormat
import java.util.Locale

fun Double.toRupiahFormat(
    useSymbol: Boolean = false,
    useDecimal: Boolean = true,
    freeIfZero: Boolean = false,
): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    var formatted = numberFormat.format(this).toString()

    if (this == 0.0 && freeIfZero) {
        return "Gratis"
    }

    if (useSymbol) {
        formatted = formatted.lowercase().replace("rp", "Rp")
    } else {
        formatted = formatted.lowercase().replace("rp", "")
    }

    if (!useDecimal) {
        formatted = formatted.substringBefore(",")
    }
    return formatted
}

fun Double.isPositive(): Boolean {
    return this >= 0
}

fun Double.isNegative(): Boolean {
    return this < 0
}

fun Double.toPositive(): Double {
    return if (this < 0) {
        this * -1
    } else {
        this
    }
}

fun Double.toNegative(): Double {
    return if (this < 0) {
        this
    } else {
        this * -1
    }
}