package com.fadlurahmanf.bebas_storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant

@Entity(tableName = BebasDbConstant.tMapp)
data class BebasEntity(
    @PrimaryKey
    var deviceId: String,
    var encryptedGuestToken: String? = null,
    var encryptedAccessToken: String? = null,
    var encryptedRefreshToken: String? = null,
    var expiresAt: String? = null,
    var refreshExpiresAt: String? = null,
)
