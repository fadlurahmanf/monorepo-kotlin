package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.TransactionApi
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTvCableRequest
import com.fadlurahmanf.bebas_api.domain.network.TransactionNetwork
import com.google.gson.JsonObject
import javax.inject.Inject

class TransactionRemoteDatasource @Inject constructor(
    context: Context
) : TransactionNetwork<TransactionApi>(context) {
    override fun getApi(): Class<TransactionApi> = TransactionApi::class.java

    fun getBankAccounts() = networkService().getBankAccounts()

    fun inquiryBankMas(request: InquiryBankMasRequest) = networkService().inquiryBankMas(request)

    fun inquiryOtherBank(request: InquiryOtherBankRequest) =
        networkService().inquiryOtherBank(request)

    fun getChallengeCode(json: JsonObject) =
        networkService().generateChallengeCode(json)

    fun fundTransferBankMAS(json: JsonObject) =
        networkService().fundTransferBankMAS(json)

    fun getTransactionDetail(transactionId: String, transactionType: String) =
        networkService().getTransactionDetail(transactionId, transactionType)

    fun getPulsaDenom(provider: String) =
        networkService().getDenomPulsa(provider)

    fun postingPulsaPrePaid(json: JsonObject) =
        networkService().postingPulsaPrePaid(json)

    fun inquiryTelkomIndihome(request: InquiryTelkomIndihomeRequest) =
        networkService().inquiryTelkomIndihome(request)

    fun inquiryPPOBProduct(billingCategory: String, providerName: String) =
        networkService().inquiryPPOBProductCode(
            category = billingCategory,
            providerName = providerName
        )

    fun postingTelkomIndihome(json: JsonObject) =
        networkService().postingTelkomIndihome(json)

    fun refreshStatus(transactionId: String) =
        networkService().refreshStatusPrePaid(transactionId)

    fun getAllHistory(offset: Int, status: String? = null) =
        networkService().getHistoryLoyalty(
            offset = offset,
            status = status
        )

    fun getDenomPlnPrePaid() =
        networkService().getDenomPrePaidPLN()

    fun inquiryTvCable(request: InquiryTvCableRequest) =
        networkService().inquiryTvCable(request)
}