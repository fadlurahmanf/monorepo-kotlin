package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse

data class InquiryResultModel(
    val additionalTransfer: InquiryTransferBank? = null,
    val inquiryPulsaData: InquiryPulsaDataResponse? = null,
    val inquiryTelkomIndihome: InquiryTelkomIndihomeResponse? = null,
    val inquiryPLNPrePaidCheckout: InquiryCheckoutFlowResponse? = null,
    val inquiryPLNPostPaidCheckout: InquiryCheckoutFlowResponse? = null,
) {
    data class InquiryTransferBank(
        val inquiryBank: InquiryBankResponse? = null,
        val isInquiryBankMas: Boolean = false,
    )
}