package com.fadlurahmanf.bebas_api.data.dto.ocr

import com.google.gson.annotations.SerializedName

data class OcrResponse(
    @SerializedName("idNumber")
    var idCardNumber: String? = null,
    var name: String? = null,
    var birthPlace: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,
    var religion: String? = null,
    var maritalStatus: String? = null,
    var bloodGroup: String? = null,
    var province: String? = null,
    var city: String? = null,
    var district: String? = null,
    var subDistrict: String? = null,
    var address: String? = null,
    var rtrw: String? = null,
)
