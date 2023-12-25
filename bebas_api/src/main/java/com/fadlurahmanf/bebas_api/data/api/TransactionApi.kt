package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryPulsaDataRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingRequest
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionApi {
    @GET("customer-info/bank-account")
    fun getBankAccounts(): Observable<BaseResponse<List<BankAccountResponse>>>

    @POST("transfer/inquiry/bank-mas")
    fun inquiryBankMas(
        @Body request: InquiryBankMasRequest
    ): Observable<BaseResponse<InquiryBankResponse>>

    @POST("transfer/inquiry/bank-lain")
    fun inquiryOtherBank(
        @Body request: InquiryOtherBankRequest
    ): Observable<BaseResponse<InquiryBankResponse>>

    @POST("mobile-provider/check-provider")
    fun inquiryPulsaData(
        @Body request: InquiryPulsaDataRequest
    ): Observable<BaseResponse<InquiryPulsaDataResponse>>

    @POST("verification/challenge-code")
    fun generateChallengeCode(
        @Body request: GenerateChallengeCodeRequest<FundTransferBankMASRequest>
    ): Observable<BaseResponse<String>>

    @POST("transfer/posting/bank-mas")
    fun fundTransferBankMAS(
        @Body json: JsonObject
    ): Observable<BaseResponse<FundTransferResponse>>

    @GET("transaction/{transactionId}/type/{transactionType}")
    fun getTransactionDetail(
        @Path("transactionId") transactionId: String,
        @Path("transactionType") transactionType: String,
    ): Observable<BaseResponse<Nothing>>
}