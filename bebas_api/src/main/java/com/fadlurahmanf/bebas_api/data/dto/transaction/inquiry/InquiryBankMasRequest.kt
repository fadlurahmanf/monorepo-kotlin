package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

data class InquiryBankMasRequest(
    val accountNumber: String,
    val destinationAccountNumber: String
)
