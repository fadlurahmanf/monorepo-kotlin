package com.fadlurahmanf.bebas_api.data.dto.transaction

data class RefreshStatusResponse(
    val transactionId: String? = null,
    val transactionStatus: String? = null,
    val serialNumber: String? = null,
    val reason: String? = null,
)
