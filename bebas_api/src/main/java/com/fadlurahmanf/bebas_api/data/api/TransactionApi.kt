package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PulsaDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("verification/challenge-code")
    fun generateChallengeCode(
        @Body json: JsonObject
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

    @GET("pulsa/denom")
    fun getDenomPulsa(
        @Query("provider") provider: String,
    ): Observable<BaseResponse<List<PulsaDenomResponse>>>

    @POST("pulsa/posting/pre-paid")
    fun postingPulsaPrePaid(
        @Body json: JsonObject
    ): Observable<BaseResponse<FundTransferResponse>>
}