package com.fadlurahmanf.mapp_notification.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SnoozeActionModel(
    val type: String = "SNOOZE",
    val map: @RawValue Map<String, Any>? = null
) : Parcelable
