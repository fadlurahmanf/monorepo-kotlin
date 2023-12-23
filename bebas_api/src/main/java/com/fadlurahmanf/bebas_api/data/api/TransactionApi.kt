package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingRequest
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
        @Body request: GenerateChallengeCodeRequest<FundTransferBankMASRequest>
    ): Observable<BaseResponse<String>>

    @POST("transfer/posting/bank-mas")
    fun fundTransferBankMAS(
        @Body request: PostingRequest<FundTransferBankMASRequest>
    ): Observable<BaseResponse<FundTransferResponse>>
}