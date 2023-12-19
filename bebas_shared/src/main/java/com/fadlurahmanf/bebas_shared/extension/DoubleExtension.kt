package com.fadlurahmanf.bebas_shared.extension

import java.text.NumberFormat
import java.util.Locale

fun Double.toRupiahFormat(
    useSymbol: Boolean = false,
    useDecimal: Boolean = true
): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    var formatted = numberFormat.format(this).toString()
    if (useSymbol) {
        formatted = formatted.lowercase().replace("rp", "Rp ")
    } else {
        formatted = formatted.lowercase().replace("rp", "")
    }

    if (!useDecimal) {
        formatted = formatted.substringBefore(",")
    }
    return formatted
}