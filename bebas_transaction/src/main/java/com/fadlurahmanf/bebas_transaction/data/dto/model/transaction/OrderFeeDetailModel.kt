package com.fadlurahmanf.bebas_transaction.data.dto.model.transaction

import android.os.Parcelable
import androidx.annotation.StyleRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFeeDetailModel(
    val total: Double,
    val details: ArrayList<Detail> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class Detail(
        val label: String,
        val value: Double,
        @StyleRes val valueStyle: Int? = null,
    ) : Parcelable
}
