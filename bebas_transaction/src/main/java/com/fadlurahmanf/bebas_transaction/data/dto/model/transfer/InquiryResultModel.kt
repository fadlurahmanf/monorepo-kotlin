package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse

data class InquiryResultModel(
    val additionalInquiryTransferBank: InquiryTransferBank? = null,
    val inquiryPulsaData: InquiryPulsaDataResponse? = null,
    val inquiryTelkomIndihome: InquiryTelkomIndihomeResponse? = null
) {
    data class InquiryTransferBank(
        val inquiryBank: InquiryBankResponse? = null,
        val isInquiryBankMas: Boolean = false,
    )
}