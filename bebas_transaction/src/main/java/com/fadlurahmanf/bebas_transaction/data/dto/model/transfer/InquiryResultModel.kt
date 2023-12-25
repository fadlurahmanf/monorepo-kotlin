package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryPulsaDataResponse

data class InquiryResultModel(
    val inquiryTransferBank: InquiryTransferBank? = null,
    val inquiryPulsaData: InquiryPulsaDataResponse? = null
) {
    data class InquiryTransferBank(
        val inquiryBank: InquiryBankResponse? = null,
        val isInquiryBankMas: Boolean = false,
    )
}