package com.fadlurahmanf.bebas_storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant

@Entity(tableName = BebasDbConstant.tMapp)
data class BebasEntity(
    @PrimaryKey
    var deviceId: String,
    var language: String = "id-ID",
    var encodedPublicKey: String? = null,
    var encodedPrivateKey: String? = null,
    var encryptedGuestToken: String? = null,
    var encryptedAccessToken: String? = null,
    var encryptedRefreshToken: String? = null,
    var expiresAt: String? = null,
    var refreshExpiresAt: String? = null,
    /**
     * POSSIBLE VALUE: CREATE_ACCOUNT/LOGIN_DIFFERENT_ACCOUNT
     * */
    var onboardingFlow: String? = null,
    var isFinishedReadTnc: Boolean? = null,
    var lastScreen: String? = null,
)
