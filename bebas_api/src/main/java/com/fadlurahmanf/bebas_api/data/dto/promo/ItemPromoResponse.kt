package com.fadlurahmanf.bebas_api.data.dto.promo

import com.google.gson.annotations.SerializedName

data class ItemPromoResponse(
    val id: String? = null,
    @SerializedName("imageUrlThumbnail")
    val thumbnail: String? = null,
    @SerializedName("imageUrlOriginal")
    val imageOriginal: String? = null,
    val title: String? = null,
    val description: String? = null,
    @SerializedName("startPeriode")
    val startPeriod: String? = null,
    @SerializedName("endPeriode")
    val endPeriod: String? = null,
    val promoCategory: String? = null,
    val cta: CTA? = null,
    val tnc: String? = null,
    val howToUse: String? = null,
    val customerServiceInfo: String? = null
) {
    data class CTA(
        @SerializedName("ctaTitle")
        val title: String? = null,
        @SerializedName("ctaLink")
        val link: String? = null,
    )
}
