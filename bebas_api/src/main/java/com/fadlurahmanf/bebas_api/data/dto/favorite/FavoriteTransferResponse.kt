package com.fadlurahmanf.bebas_api.data.dto.favorite

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteTransferResponse(
    val id: String? = null,
    val aliasName: String? = null,
    val bankName: String? = null,
    @SerializedName("accountNumber")
    val bankAccountNumber: String? = null,
    val sknId: String? = null,
    val rtgsId: String? = null,
    val isPinned: Boolean? = null,
) : Parcelable
