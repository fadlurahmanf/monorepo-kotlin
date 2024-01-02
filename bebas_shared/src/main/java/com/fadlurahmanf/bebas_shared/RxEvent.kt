package com.fadlurahmanf.bebas_shared

class RxEvent {
    data class ChangeLanguageEvent(
        val languageCode: String,
        val countryCode: String,
        val language: String
    )

    data class ResetTimerForceLogout(
        val expiresIn: Long,
        val refreshExpiresIn: Long,
    )

    object ForceLogoutBottomsheet
}