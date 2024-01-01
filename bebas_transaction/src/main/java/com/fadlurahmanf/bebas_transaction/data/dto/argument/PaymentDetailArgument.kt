package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetailArgument(
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean = false,
    var labelIdentity: String,
    var subLabelIdentity: String,
    var ppobImageUrl: String? = null,
    var additionalPulsaData: AdditionalPulsaDataArgument? = null,
) : Parcelable {
    @Parcelize
    data class AdditionalPulsaDataArgument(
        val providerImage: String? = null,
        val providerName: String,
        val phoneNumber: String
    ) : Parcelable
}
