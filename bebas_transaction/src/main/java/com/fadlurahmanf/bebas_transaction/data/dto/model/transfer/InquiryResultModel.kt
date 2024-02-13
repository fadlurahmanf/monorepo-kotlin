package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryCheckoutFlowResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTvCableResponse

data class InquiryResultModel(
    val additionalTransfer: InquiryTransferBank? = null,
    val inquiryPulsaData: InquiryPulsaDataResponse? = null,
    val inquiryTelkomIndihome: InquiryTelkomIndihomeResponse? = null,
    val inquiryTvCable: InquiryTvCableResponse? = null,
    val inquiryPLNPrePaidCheckout: InquiryCheckoutFlowResponse? = null,
    val inquiryPLNPostPaidCheckout: InquiryCheckoutFlowResponse? = null,
) {
    data class InquiryTransferBank(
        val inquiryBank: InquiryBankResponse? = null,
        val isInquiryBankMas: Boolean = false,
    )
}