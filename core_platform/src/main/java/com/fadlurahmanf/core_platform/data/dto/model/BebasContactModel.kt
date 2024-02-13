package com.fadlurahmanf.core_platform.data.dto.model

data class BebasContactModel(
    val name: String,
    var nameHtml: String,
    val phoneNumber: String,
    var phoneNumberHtml: String,
    // type == 0 -> initial, type == 1, contact
    val type: Int = 1
)
