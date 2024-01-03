package com.fadlurahmanf.bebas_api.data.dto.general

data class BasePaginationTransactionResponse<T>(
    val totalPages:Int ?= null,
    val totalItems:Int ?= null,
    val next:Boolean ?= null,
    var content: T ?= null
)
