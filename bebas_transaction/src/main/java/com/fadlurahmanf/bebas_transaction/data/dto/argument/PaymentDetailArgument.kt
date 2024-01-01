package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetailArgument(
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean = false,
    var labelIdentity: String,
    var subLabelIdentity: String,
    var additionalPulsaData: AdditionalPulsaDataArgument? = null,
    var additionalTelkomIndihome: AdditionalTelkomIndihome? = null,
) : Parcelable {
    @Parcelize
    data class AdditionalPulsaDataArgument(
        val providerImage: String? = null,
        val providerName: String,
        val phoneNumber: String
    ) : Parcelable

    @Parcelize
    data class AdditionalTelkomIndihome(
        val providerImage: String? = null,
        val periode: String,
        val tagihan: Double,
        val inquiry: InquiryTelkomIndihomeResponse,
    ) : Parcelable
}
