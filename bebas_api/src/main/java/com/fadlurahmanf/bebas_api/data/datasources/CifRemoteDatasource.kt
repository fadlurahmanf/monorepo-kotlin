package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.CifApi
import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.domain.network.CifNetwork
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.InputStream
import javax.inject.Inject

class CifRemoteDatasource @Inject constructor(
    context: Context
) : CifNetwork<CifApi>(context) {
    override fun getApi(): Class<CifApi> = CifApi::class.java

    fun getEStatements(accountNumber: String): Observable<BaseResponse<EStatementResponse>> {
        val json = JsonObject()
        json.addProperty("accountNumber", accountNumber)
        return networkService().getEStatements(json)
    }

    fun downloadEStatement(accountNumber: String, year: Int, month: Int): InputStream? {
        val json = JsonObject()
        json.addProperty("accountNumber", accountNumber)
        val period = JsonObject()
        period.addProperty("year", year)
        period.addProperty("month", month)
        json.add("period", period)
        return networkService().downloadEStatement(json).execute().body()?.byteStream()
    }

    fun getFavoriteTransfer() = networkService().getFavoriteTransfer()
    fun getFavoritePLNPrePaid() = networkService().getFavoritePLNPrePaid()
    fun getFavoritePLNPostPaid() = networkService().getFavoritePLNPostPaid()
    fun getFavoritePulsaPrePaid() = networkService().getFavoritePulsaPrePaid()
    fun getFavoriteTelkomIndihome() = networkService().getFavoriteTelkomIndihome()
    fun getFavoriteTopUpEWallet() = networkService().getFavoriteTopUpEWallet()

    fun pinFavorite(request: PinFavoriteRequest) = networkService().pinFavorite(request)
    fun getLatestTransactionTransfer() = networkService().getLatestTransactionTransfer()
    fun getLatestTransactionPLNPrePaid() = networkService().getLatestPrePaidTransaction(
        type = "Listrik"
    )

    fun getLatestTransactionPLNPostPaid() = networkService().getLatestPostPaidTransaction(
        type = "Listrik"
    )

    fun getLatestTransactionTVCable() = networkService().getLatestPostPaidTransaction(
        type = "TV Kabel/Internet"
    )

    fun getLatestTransactionPulsaPrePaid() = networkService().getLatestPulsaPrePaidTransaction()

    fun getLatestTransactionTelkomIndihome() = networkService().getLatestPostPaidTransaction(
        type = "Telepon/Internet"
    )

    fun getCifBebasPoin() = networkService().getCifBebasPoin()
}