package com.fadlurahmanf.bebas_transaction.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.FulfillmentRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.IdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.pin.PinAttemptResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.PulsaDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.GenerateChallengeCodeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryOtherBankRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PLNDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.RefreshStatusResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.ItemBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingRequest
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryResultModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationResultModel
import com.fadlurahmanf.bebas_transaction.data.flow.PPOBDenomFlow
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val cmsRemoteDatasource: CmsRemoteDatasource,
    private val cryptoTransactionRepositoryImpl: CryptoTransactionRepositoryImpl,
    private val fulfillmentRemoteDatasource: FulfillmentRemoteDatasource,
    private val identityRemoteDatasource: IdentityRemoteDatasource,
    private val transactionRemoteDatasource: TransactionRemoteDatasource,
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

    fun getPaymentSourceModelFromGetBankAccounts(): Observable<List<PaymentSourceModel>> {
        return transactionRemoteDatasource.getBankAccounts().map { baseResp ->
            if (baseResp.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }
            baseResp.data!!.map { bankAccount ->
                PaymentSourceModel(
                    accountName = bankAccount.accountName ?: "-",
                    accountNumber = bankAccount.accountNumber ?: "-",
                    subLabel = "MAS Saving • ${bankAccount.accountNumber ?: "-"}",
                    balance = bankAccount.workingBalance ?: -1.0,
                    bankAccountResponse = bankAccount
                )
            }
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

    fun inquiryBankMasReturnModel(destinationAccountNumber: String): Observable<InquiryResultModel> {
        return inquiryBankMas(destinationAccountNumber).map { inqRes ->
            InquiryResultModel(
                additionalInquiryTransferBank = InquiryResultModel.InquiryTransferBank(
                    inquiryBank = inqRes,
                    isInquiryBankMas = true,
                )
            )
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

    fun inquiryPulsaData(
        phoneNumber: String
    ): Observable<InquiryPulsaDataResponse> {
        var formattedPhoneNumber = phoneNumber.replace("+", "").replace(" ", "").replace("-", "")
        if (formattedPhoneNumber.startsWith("628")) {
            formattedPhoneNumber = formattedPhoneNumber.replaceFirst("628", "08")
        }
        val request = InquiryPulsaDataRequest(
            phoneNumber = formattedPhoneNumber
        )
        return cmsRemoteDatasource.inquiryPulsaData(request).map { inqRes ->
            if (inqRes.data == null) {
                throw BebasException.generalRC("INQ_00")
            }
            inqRes.data!!
        }
    }

    fun inquiryPulsaDataReturnModel(
        phoneNumber: String
    ): Observable<InquiryResultModel> {
        return inquiryPulsaData(phoneNumber).map { inqRes ->
            InquiryResultModel(
                inquiryPulsaData = inqRes
            )
        }
    }

    fun generateChallengeCode(json: JsonObject): Observable<String> {
        return transactionRemoteDatasource.getChallengeCode(json).map {
            if (it.data == null) {
                throw BebasException.generalRC("CC_00")
            }
            it.data!!
        }
    }

    fun postingTransferBankMas(
        plainPin: String,
        fundTransferRequest: FundTransferBankMASRequest
    ): Observable<PostingPinVerificationResultModel> {
        if (!cryptoTransactionRepositoryImpl.verifyPin(plainPin)) {
            throw BebasException.generalRC("INCORRECT_PIN")
        }
        val timestamp = System.currentTimeMillis().toString()
        val challengeCodeRequest = GenerateChallengeCodeRequest<FundTransferBankMASRequest>(
            data = fundTransferRequest,
            transactionType = "Antar Rekening",
            timestamp = timestamp,
        )
        return generateChallengeCode(Gson().toJsonTree(challengeCodeRequest).asJsonObject).flatMap {
            val fundTransferRequestString = Gson().toJson(fundTransferRequest)
            val signature = cryptoTransactionRepositoryImpl.generateSignature(
                plainJsonString = fundTransferRequestString,
                timestamp = timestamp,
                challengeCode = it
            )
            val body = PostingRequest<FundTransferBankMASRequest>(
                data = fundTransferRequest,
                signature = signature,
                timestamp = timestamp
            )
            transactionRemoteDatasource.fundTransferBankMAS(Gson().toJsonTree(body).asJsonObject)
                .map { resp ->
                    if (resp.data == null) {
                        throw BebasException.generalRC("FT_00")
                    }
                    PostingPinVerificationResultModel(
                        transactionStatus = resp.message ?: "-",
                        tranferBankMas = resp.data!!
                    )
                }
        }
    }

    fun getTotalPinAttempt(): Observable<PinAttemptResponse> {
        return identityRemoteDatasource.getTotalPinAttempt().map { resp ->
            if (resp.data == null) {
                throw BebasException.generalRC("TP_00")
            }
            resp.data!!
        }
    }

    fun getPulsaDenom(
        provider: String
    ): Observable<List<PulsaDenomResponse>> {
        return transactionRemoteDatasource.getPulsaDenom(provider).map { resp ->
            if (resp.data == null) {
                throw BebasException.generalRC("TP_00")
            }
            resp.data!!
        }
    }

    fun getPulsaDenomModel(
        provider: String,
        providerImage: String? = null
    ): Observable<List<PPOBDenomModel>> {
        return getPulsaDenom(provider).map { denoms ->
            denoms.map { denom ->
                PPOBDenomModel(
                    flow = PPOBDenomFlow.PULSA_PREPAID,
                    id = "${denom.nominal ?: ""}",
                    totalBayar = (denom.nominal ?: -1.0) + (denom.adminFee ?: -1.0),
                    nominal = denom.nominal ?: -1.0,
                    imageUrl = providerImage,
                    isAvailable = true,
                    pulsaDenomResponse = denom
                )
            }
        }
    }

    private fun getPlnPrePaidDenom(): Observable<List<PLNDenomResponse>> {
        return transactionRemoteDatasource.getDenomPlnPrePaid().map { resp ->
            if (resp.data == null) {
                throw BebasException.generalRC("TP_00")
            }
            resp.data!!
        }
    }

    fun getPLNPrePaidDenomModel(): Observable<List<PPOBDenomModel>> {
        return getPlnPrePaidDenom().map { denoms ->
            denoms.map { denom ->
                PPOBDenomModel(
                    flow = PPOBDenomFlow.PLN_PREPAID,
                    id = "${denom.nominal ?: ""}",
                    totalBayar = (denom.nominal ?: -1.0) + (denom.adminFee ?: -1.0),
                    nominal = denom.nominal ?: -1.0,
                    isAvailable = denom.isAvailable == true,
                    plnPrePaidDenomResponse = denom
                )
            }
        }
    }

    fun postingPulsaPrePaid(
        plainPin: String,
        pulsaPrePaidRequest: PostingPulsaPrePaidRequest
    ): Observable<PostingPinVerificationResultModel> {
        if (!cryptoTransactionRepositoryImpl.verifyPin(plainPin)) {
            throw BebasException.generalRC("INCORRECT_PIN")
        }
        val timestamp = System.currentTimeMillis().toString()
        val challengeCodeRequest = GenerateChallengeCodeRequest<PostingPulsaPrePaidRequest>(
            data = pulsaPrePaidRequest,
            transactionType = "Antar Rekening",
            timestamp = timestamp,
        )
        return generateChallengeCode(Gson().toJsonTree(challengeCodeRequest).asJsonObject).flatMap {
            val pulsaPrePaidRequestString = Gson().toJson(pulsaPrePaidRequest)
            val signature = cryptoTransactionRepositoryImpl.generateSignature(
                plainJsonString = pulsaPrePaidRequestString,
                timestamp = timestamp,
                challengeCode = it
            )
            val body = PostingRequest<PostingPulsaPrePaidRequest>(
                data = pulsaPrePaidRequest,
                signature = signature,
                timestamp = timestamp
            )
            transactionRemoteDatasource.postingPulsaPrePaid(Gson().toJsonTree(body).asJsonObject)
                .map { resp ->
                    if (resp.data == null) {
                        throw BebasException.generalRC("FT_00")
                    }
                    PostingPinVerificationResultModel(
                        transactionStatus = resp.message ?: "-",
                        pulsaPrePaid = resp.data!!
                    )
                }
        }
    }

    fun inquiryTelkomIndihome(customerId: String): Observable<InquiryTelkomIndihomeResponse> {
        return getMainBankAccount().flatMap { bankAccount ->
            val request = InquiryTelkomIndihomeRequest(
                fromAccountNumber = bankAccount.accountNumber ?: "-",
                customerId = customerId
            )
            transactionRemoteDatasource.inquiryTelkomIndihome(request).map { inqRes ->
                if (inqRes.data == null) {
                    throw BebasException.generalRC("INQ_00")
                }
                inqRes.data!!
            }
        }
    }

    fun inquiryTelkomIndihomeReturnModel(
        customerId: String
    ): Observable<InquiryResultModel> {
        return inquiryTelkomIndihome(customerId).map { resp ->
            InquiryResultModel(
                inquiryTelkomIndihome = resp
            )
        }
    }

    private fun inquiryPLNPostPaidCheckoutFlow(
        customerId: String,
        providerName: String,
        billingCategory: String
    ): Observable<InquiryCheckoutFlowResponse> {
        return transactionRemoteDatasource.inquiryPPOBProduct(
            providerName = providerName,
            billingCategory = billingCategory
        ).flatMap { productCodeBaseResp ->
            if (productCodeBaseResp.data == null) {
                throw BebasException.generalRC("PPOB_PRODUCT_CODE_00")
            }
            val productCodeResp = productCodeBaseResp.data!!
            val request = InquiryCheckoutFlowRequest(
                productCode = productCodeResp.providerProductCode ?: "-",
                clientNumber = customerId
            )
            fulfillmentRemoteDatasource.inquiryCheckoutFlow(request).map { inqRes ->
                if (inqRes.data == null) {
                    throw BebasException.generalRC("INQ_00")
                }
                inqRes.data!!
            }
        }
    }

    fun inquiryPLNPostPaidCheckoutFlowReturnModel(
        customerId: String,
        providerName: String,
        billingCategory: String
    ): Observable<InquiryResultModel> {
        return inquiryPLNPostPaidCheckoutFlow(
            customerId = customerId,
            providerName = providerName,
            billingCategory = billingCategory
        ).map {
            InquiryResultModel(
                inquiryPLNPostPaidCheckout = it
            )
        }
    }

    fun inquiryPLNPrePaidCheckout(
        customerId: String,
        productCode: String,
    ): Observable<InquiryCheckoutFlowResponse> {
        val request = InquiryCheckoutFlowRequest(
            productCode = productCode,
            clientNumber = customerId
        )
        return fulfillmentRemoteDatasource.inquiryCheckoutFlow(request).map { inqRes ->
            if (inqRes.data == null) {
                throw BebasException.generalRC("INQ_00")
            }
            inqRes.data!!
        }
    }

    fun inquiryPLNPrePaidCheckoutReturnModel(
        customerId: String,
        productCode: String,
    ): Observable<InquiryResultModel> {
        val request = InquiryCheckoutFlowRequest(
            productCode = productCode,
            clientNumber = customerId
        )
        return inquiryPLNPrePaidCheckout(customerId, productCode).map {
            InquiryResultModel(
                inquiryPLNPrePaidCheckout = it
            )
        }
    }

    fun postingTelkomIndihome(
        plainPin: String,
        telkomIndihomeRequest: PostingTelkomIndihomeRequest
    ): Observable<PostingPinVerificationResultModel> {
        if (!cryptoTransactionRepositoryImpl.verifyPin(plainPin)) {
            throw BebasException.generalRC("INCORRECT_PIN")
        }
        val timestamp = System.currentTimeMillis().toString()
        val challengeCodeRequest = GenerateChallengeCodeRequest<PostingTelkomIndihomeRequest>(
            data = telkomIndihomeRequest,
            transactionType = "Antar Rekening",
            timestamp = timestamp,
        )
        return generateChallengeCode(Gson().toJsonTree(challengeCodeRequest).asJsonObject).flatMap {
            val telkomIndihomeRequestString = Gson().toJson(telkomIndihomeRequest)
            val signature = cryptoTransactionRepositoryImpl.generateSignature(
                plainJsonString = telkomIndihomeRequestString,
                timestamp = timestamp,
                challengeCode = it
            )
            val body = PostingRequest<PostingTelkomIndihomeRequest>(
                data = telkomIndihomeRequest,
                signature = signature,
                timestamp = timestamp
            )
            transactionRemoteDatasource.postingTelkomIndihome(Gson().toJsonTree(body).asJsonObject)
                .map { resp ->
                    if (resp.data == null) {
                        throw BebasException.generalRC("FT_00")
                    }
                    PostingPinVerificationResultModel(
                        transactionStatus = resp.message ?: "-",
                        telkomIndihome = resp.data!!
                    )
                }
        }
    }

    fun refreshStatusTransaction(
        transactionId: String
    ): Observable<RefreshStatusResponse> {
        return transactionRemoteDatasource.refreshStatus(transactionId).map { resp ->
            if (resp.data == null) {
                throw BebasException.generalRC("RT_00")
            }
            resp.data!!
        }
    }

}