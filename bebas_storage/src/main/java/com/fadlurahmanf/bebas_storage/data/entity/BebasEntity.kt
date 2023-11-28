package com.fadlurahmanf.bebas_storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant

@Entity(tableName = BebasDbConstant.tMapp)
data class BebasEntity(
    @PrimaryKey
    var deviceId: String,
    var language: String = "in-ID",
    var encodedPublicKey: String? = null,
    var encodedPrivateKey: String? = null,
    var encryptedGuestToken: String? = null,
    var encryptedAccessToken: String? = null,
    var encryptedRefreshToken: String? = null,
    var expiresAt: String? = null,
    var refreshExpiresAt: String? = null,
    /**
     * POSSIBLE VALUE: CREATE_ACCOUNT/ALREADY_HAVE_ACCOUNT_NUMBER
     * */
    var onboardingFlow: OnboardingFlow? = null,
    var encryptedPhone: String? = null,
    var encryptedEmail: String? = null,
    var isFinishedReadTnc: Boolean? = null,
    var lastScreen: String? = null,
    var encryptedOtpToken: String? = null,
    var encryptedEmailToken: String? = null,
    var encryptedOnboardingId: String? = null,
    var isFinishedOtpVerification: Boolean? = null,
    var isFinishedEmailVerification: Boolean? = null,
    var isFinishedPrepareOnboarding: Boolean? = null,
    var base64ImageEktp: String? = null,
    var isFinishedEktpCameraVerification: Boolean? = null,
    var encryptedIdCardNumber: String? = null,
    var encryptedFullName: String? = null,
    var birthPlace: String? = null,
    var birthDate: String? = null,
    var gender: String? = null,
    var encryptedProvince: String? = null,
    var encryptedCity: String? = null,
    var encryptedSubDistrict: String? = null,
    var encryptedWard: String? = null,
    var encryptedAddress: String? = null,
    var encryptedRtRw: String? = null,
)
