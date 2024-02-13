package com.fadlurahmanf.bebas_shared.extension

import android.content.res.Resources
import kotlin.math.roundToInt

fun Int.toDp(): Int {
    return (this * Resources.getSystem().displayMetrics.density).roundToInt()
}