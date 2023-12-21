package com.fadlurahmanf.bebas_transaction.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.ItemBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingRequest
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.serializeToMap
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteDatasource: TransactionRemoteDatasource,
    private val cmsRemoteDatasource: CmsRemoteDatasource,
) {

    fun getBankList(): Observable<List<ItemBankResponse>> {
        return cmsRemoteDatasource.getBankList().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }
            it.data!!.sortedBy { bankResp ->
                bankResp.nickName
            }
        }
    }

    fun getTopBanks(banks: List<ItemBankResponse>): List<ItemBankResponse> {
        return ArrayList(banks).filter {
            (it.sknId == "0140397" || it.rtgsId == "CENAIDJA") // bca
            || (it.sknId == "0090010" || it.rtgsId == "BNINIDJA") // bni
            || (it.sknId == "0020307" || it.rtgsId == "BRINIDJA") // bri
            || (it.sknId == "0080017" || it.rtgsId == "BMRIIDJA") // manidiri
            || (it.sknId == "5480300" || it.rtgsId == "BMSEIDJA") // mas
        }
    }

    fun removeTopBanks(banks: List<ItemBankResponse>): List<ItemBankResponse> {
        return ArrayList(banks).filter {
            !(it.sknId == "0140397" || it.rtgsId == "CENAIDJA") // bca
            && !(it.sknId == "0090010" || it.rtgsId == "BNINIDJA") // bni
            && !(it.sknId == "0020307" || it.rtgsId == "BRINIDJA") // bri
            && !(it.sknId == "0080017" || it.rtgsId == "BMRIIDJA") // manidiri
            && !(it.sknId == "5480300" || it.rtgsId == "BMSEIDJA") // mas
        }
    }

    fun getBankAccounts(): Observable<List<BankAccountResponse>> {
        return transactionRemoteDatasource.getBankAccounts().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!
        }
    }

    fun getMainBankAccount(): Observable<BankAccountResponse> {
        return transactionRemoteDatasource.getBankAccounts().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            if (it.data?.isEmpty() == true) {
                throw BebasException.generalRC("MBA_00")
            }
            val bankAccounts = it.data!!
            bankAccounts.first()
        }
    }


    fun inquiryBankMas(destinationAccountNumber: String): Observable<InquiryBankResponse> {
        return getMainBankAccount().flatMap { bankAccount ->
            val request = InquiryBankMasRequest(
                accountNumber = bankAccount.accountNumber ?: "-",
                destinationAccountNumber = destinationAccountNumber
            )
            transactionRemoteDatasource.inquiryBankMas(request).map { inqRes ->
                if (inqRes.data == null) {
                    throw BebasException.generalRC("INQ_00")
                }
                inqRes.data!!
            }
        }
    }

    fun inquiryOtherBank(
        sknId: String,
        destinationAccountNumber: String
    ): Observable<InquiryBankResponse> {
        return getMainBankAccount().flatMap { bankAccount ->
            val request = InquiryOtherBankRequest(
                sknId = sknId,
                accountNumber = bankAccount.accountNumber ?: "-",
                destinationAccountNumber = destinationAccountNumber
            )
            transactionRemoteDatasource.inquiryOtherBank(request).map { inqRes ->
                if (inqRes.data == null) {
                    throw BebasException.generalRC("INQ_00")
                }
                inqRes.data!!
            }
        }
    }

    fun generateChallengeCode(json: JSONObject): Observable<String> {
        return transactionRemoteDatasource.getChallengeCode(json).map {
            if (it.data == null) {
                throw BebasException.generalRC("CC_00")
            }
            it.data!!
        }
    }

    fun postingTransferBankMas(
        accountNumber: String,
        amountTransaction: Long,
        notes: String,
        destinationAccountName: String,
        destinationAccountNumber: String,
    ): Observable<FundTransferResponse> {
        val timeStamp = System.currentTimeMillis().toString()
        val fundTransferRequest = FundTransferBankMASRequest(
            accountNumber = accountNumber,
            destinationAccountName = destinationAccountName,
            destinationAccountNumber = destinationAccountNumber,
            notes = notes,
            amountTransaction = amountTransaction,
            ip = "0",
            latitude = 0.0,
            longitude = 0.0
        )
        val challengeCodeRequest = GenerateChallengeCodeRequest<FundTransferBankMASRequest>(
            data = fundTransferRequest,
            timestamp = timeStamp,
            type = "Antar Rekening"
        )
        val body = PostingRequest<FundTransferBankMASRequest>(
            data = fundTransferRequest,
            signature = "asaa",
            timestamp = timeStamp
        )
        val json = JSONObject()
        challengeCodeRequest.serializeToMap().entries.forEach {
            json.put(it.key, it.value)
        }
        return generateChallengeCode(json).flatMap {
            transactionRemoteDatasource.fundTransferBankMAS(body).map {
                if (it.data == null) {
                    throw BebasException.generalRC("FT_00")
                }
                it.data!!
            }
        }
    }
}