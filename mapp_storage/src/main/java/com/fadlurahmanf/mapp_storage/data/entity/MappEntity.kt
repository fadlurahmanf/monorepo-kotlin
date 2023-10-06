package com.fadlurahmanf.mapp_storage.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.mapp_storage.data.constant.MappDbConstant

@Entity(tableName = MappDbConstant.tMapp)
data class MappEntity(
    @PrimaryKey
    var deviceId: String,
    var guestToken: String? = null,
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var expiresIn: Int? = null,
    var refreshExpiresIn: Int? = null,
)
