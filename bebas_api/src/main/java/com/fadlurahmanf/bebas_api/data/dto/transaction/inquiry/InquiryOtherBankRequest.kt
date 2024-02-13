package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

data class InquiryOtherBankRequest(
    val sknId: String,
    val accountNumber: String,
    val destinationAccountNumber: String
)
