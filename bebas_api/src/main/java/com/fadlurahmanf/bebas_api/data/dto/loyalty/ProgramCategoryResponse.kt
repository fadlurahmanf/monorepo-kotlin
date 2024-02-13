package com.fadlurahmanf.bebas_api.data.dto.loyalty

import com.google.gson.annotations.SerializedName

data class ProgramCategoryResponse(
    val categoryId: String? = null,
    @SerializedName("label")
    val categoryLabel: String? = null
)
