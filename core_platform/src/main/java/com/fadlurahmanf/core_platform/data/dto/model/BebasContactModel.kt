package com.fadlurahmanf.core_platform.data.dto.model

data class BebasContactModel(
    val name: String,
    val phoneNumber: String,
    // type == 0 -> initial, type == 1, contact
    val type: Int = 1
)
