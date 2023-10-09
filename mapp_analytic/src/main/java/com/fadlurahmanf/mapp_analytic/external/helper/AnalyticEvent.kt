package com.fadlurahmanf.mapp_analytic.external.helper

import android.content.Context
import android.os.Build
import android.provider.Settings

object AnalyticEvent {
    const val ex_notif_clicked = "ex_notif_clicked"
    const val ex_biometric_clicked = "ex_biometric_clicked"
    const val ex_shortcut_clicked = "ex_shortcut_clicked"
    const val ex_rsa_clicked = "ex_rsa_clicked"
    const val ex_aes_clicked = "ex_aes_clicked"
    const val ex_ed25119_clicked = "ex_ed25119_clicked"
    const val ex_hls_vplayer_clicked = "ex_hls_vplayer_clicked"
    const val ex_mp4_vplayer_clicked = "ex_mp4_vplayer_clicked"
    const val ex_object_labeling_mlkit_clicked = "ex_object_labeling_mlkit_clicked"
    const val ex_face_detector_mlkit_clicked = "ex_face_detector_mlkit_clicked"
    const val ex_list_room_rtc_clicked = "ex_list_room_rtc_clicked"

    fun defaultParamMap(context: Context): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["deviceId"] = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "-"
        map["deviceBrand"] = "BR:${Build.BRAND}"
        map["deviceProduct"] = "PROD:${Build.PRODUCT}"
        map["deviceEntity"] = "FG:${Build.FINGERPRINT}"
        return map.toMap()
    }
}