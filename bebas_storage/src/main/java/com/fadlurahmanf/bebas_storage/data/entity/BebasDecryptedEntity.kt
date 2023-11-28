package com.fadlurahmanf.bebas_storage.data.entity

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
    var emailToken: String? = null,
    var onboardingId: String? = null,
    var isFinishedOtpVerification: Boolean? = null,
    var isFinishedEmailVerification: Boolean? = null,
    var isFinishedPrepareOnboarding: Boolean? = null,
    var base64ImageEktp: String? = null,
    var isFinishedEktpCameraVerification: Boolean? = null,
    var idCardNumber: String? = null,
    var fullName: String? = null,
    var birthPlace: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,
    var province: String? = null,
    var city: String? = null,
    var subDistrict: String? = null,
    var ward: String? = null,
    var address: String? = null,
    var rtRw: String? = null,
)
