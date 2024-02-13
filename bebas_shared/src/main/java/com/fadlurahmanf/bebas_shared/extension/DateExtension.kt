package com.fadlurahmanf.bebas_shared.extension

import android.text.format.DateUtils
import com.fadlurahmanf.bebas_shared.helper.BebasSharedHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// RETURN -> [11-01-2023]
fun Date.formatToEktpForm(): String {
    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}

// RETURN -> [2020-12-01]
fun Date.formatFetchNotification(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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

/**
 * INPUT -> DATE
 * OUTPUT MM -> 01, 02, 03
 * */
fun Date.getMM(): String {
    val simpleDateFormat = SimpleDateFormat("MM", Locale.getDefault())
    return simpleDateFormat.format(this)
}

/**
 * INPUT -> DATE
 * OUTPUT YEAR -> 2023
 * */
fun Date.getYYYY(): String {
    val simpleDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Date.formatHeaderHistoryLoyalty(): String {
    return try {
        val month = getMM()
        val year = getYYYY()
        "${BebasSharedHelper.getFullNameMonth(month.toInt())} $year"
    } catch (e: Throwable) {
        "-"
    }
}

fun Date.formatLabelHistoryLoyaltyDate(): String {
    return try {
        val day = SimpleDateFormat("dd", Locale.getDefault()).format(this)
        val month = getMM()
        val year = getYYYY()
        "$day ${BebasSharedHelper.getFullNameMonth(month.toInt())} $year"
    } catch (e: Throwable) {
        "-"
    }
}