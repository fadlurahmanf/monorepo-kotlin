package com.fadlurahmanf.bebas_api.data.dto.transfer

data class PostingFundTransferResponse(
    var transactionId: String? = null,
    var correlationId: String? = null,
    var referenceCode: String? = null,
    var transactionDateTime: String? = null,
)
