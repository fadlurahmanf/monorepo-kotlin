package com.fadlurahmanf.bebas_loyalty.data.dto

import com.fadlurahmanf.bebas_api.data.dto.loyalty.ProgramCategoryResponse

data class ProgramCategoryModel(
    var id: String,
    var label: String,
    var isSelected: Boolean = false,
    val item: ProgramCategoryResponse
)
