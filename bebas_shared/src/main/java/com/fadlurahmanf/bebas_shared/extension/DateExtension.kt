package com.fadlurahmanf.bebas_shared.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.formatToEktpForm(): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}