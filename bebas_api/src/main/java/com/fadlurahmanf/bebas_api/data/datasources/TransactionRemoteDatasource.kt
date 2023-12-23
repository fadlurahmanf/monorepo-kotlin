package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.TransactionApi
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingRequest
import com.fadlurahmanf.bebas_api.domain.network.TransactionNetwork
import org.json.JSONObject
import retrofit2.http.Body
import javax.inject.Inject

class TransactionRemoteDatasource @Inject constructor(
    context: Context
) : TransactionNetwork<TransactionApi>(context) {
    override fun getApi(): Class<TransactionApi> = TransactionApi::class.java

    fun getBankAccounts() = networkService().getBankAccounts()

    fun inquiryBankMas(request: InquiryBankMasRequest) = networkService().inquiryBankMas(request)

    fun inquiryOtherBank(request: InquiryOtherBankRequest) =
        networkService().inquiryOtherBank(request)

    fun getChallengeCode(request: GenerateChallengeCodeRequest<FundTransferBankMASRequest>) =
        networkService().generateChallengeCode(request)

    fun fundTransferBankMAS(body: PostingRequest<FundTransferBankMASRequest>) =
        networkService().fundTransferBankMAS(body)
}