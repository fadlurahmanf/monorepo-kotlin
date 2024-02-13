package com.fadlurahmanf.bebas_api.data.dto.ektp

data class EktpDataV2Request(
    var onboardingId: String? = null,
    val idNumber: String,
    val name: String,
    val birthPlace: String,
    val birthDate: String,
    val gender: String,
    val genderCode: String,
    val maritalStatus: String,
    val maritalStatusCode: String,
    val province: String,
    val provinceCode: String,
    val city: String,
    val cityCode: String,
    val district: String,
    val districtCode: String,
    val subDistrict: String,
    val subDistrictCode: String,
    val address: String,
    val rtrw: String,
    val startDate: String = System.currentTimeMillis().toString()
)
