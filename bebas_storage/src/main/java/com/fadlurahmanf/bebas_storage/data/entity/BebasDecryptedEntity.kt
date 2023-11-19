package com.fadlurahmanf.bebas_storage.data.entity

import androidx.room.PrimaryKey
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow

data class BebasDecryptedEntity(
    var deviceId: String,
    var language: String = "in-ID",
    var publicKey: String? = null,
    var privateKey: String? = null,
    var guestToken: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var expiresAt: String? = null,
    var refreshExpiresAt: String? = null,
    /**
     * POSSIBLE VALUE: CREATE_ACCOUNT/LOGIN_DIFFERENT_ACCOUNT
     * */
    var onboardingFlow: OnboardingFlow? = null,
    var phone: String? = null,
    var email: String? = null,
    var isFinishedReadTnc: Boolean? = null,
    var lastScreen: String? = null,
    var otpToken: String? = null,
)
