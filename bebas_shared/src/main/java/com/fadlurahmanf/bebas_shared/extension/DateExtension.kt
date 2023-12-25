package com.fadlurahmanf.bebas_shared.extension

import android.text.format.DateUtils
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.formatToEktpForm(): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Date.formatNotification(): String {
    try {
        val notificationCalendar = Calendar.getInstance().apply {
            time = this@formatNotification
        }
        notificationCalendar.add(Calendar.HOUR, 7)
        val date = notificationCalendar.time
        val isToday = DateUtils.isToday(date.time)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (isToday) {
            return "Hari ini, ${timeFormat.format(date)} WIB"
        }
        val dayFormat = SimpleDateFormat("EE", Locale.getDefault())
        return "${dayFormat.format(date)}, ${timeFormat.format(date)} WIB"
    } catch (e: Exception) {
        return "-"
    }
}

fun Date.formatInvoiceTransaction(): String {
    val simpleDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return "${simpleDateFormat.format(this)} WIB"
}