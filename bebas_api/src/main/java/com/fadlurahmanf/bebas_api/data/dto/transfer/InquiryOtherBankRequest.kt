package com.fadlurahmanf.bebas_api.data.dto.transfer

data class InquiryOtherBankRequest(
    val sknId: String,
    val accountNumber: String,
    val destinationAccountNumber: String
)
