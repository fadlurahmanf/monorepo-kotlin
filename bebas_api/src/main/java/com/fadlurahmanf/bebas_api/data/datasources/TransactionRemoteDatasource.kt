package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.TransactionApi
import com.fadlurahmanf.bebas_api.data.dto.InquiryBankMasRequest
import com.fadlurahmanf.bebas_api.domain.network.TransactionNetwork
import javax.inject.Inject

class TransactionRemoteDatasource @Inject constructor(
    context: Context
) : TransactionNetwork<TransactionApi>(context) {
    override fun getApi(): Class<TransactionApi> = TransactionApi::class.java

    fun getBankAccounts() = networkService(30).getBankAccounts()

    fun inquiryBankMas(request: InquiryBankMasRequest) = networkService(30).inquiryBankMas(request)
}