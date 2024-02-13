package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

sealed class InquiryRequestModel {
    data class InquiryBankMas(
        val destinationAccountNumber: String
    ) : InquiryRequestModel()

    data class InquiryPulsaData(
        val phoneNumber: String
    ) : InquiryRequestModel()

    data class InquiryTelkomIndihome(
        val customerId: String
    ) : InquiryRequestModel()

    data class InquiryTvCable(
        val customerId: String,
        val providerName: String,
    ) : InquiryRequestModel()

    data class InquiryPLNPostPaid(
        val customerId: String,
        val providerName: String,
        val billingCategory: String,
    ) : InquiryRequestModel()
}
