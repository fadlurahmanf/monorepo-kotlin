package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.CifApi
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.domain.network.CifNetwork
import javax.inject.Inject

class CifRemoteDatasource @Inject constructor(
    context: Context
) : CifNetwork<CifApi>(context) {
    override fun getApi(): Class<CifApi> = CifApi::class.java

    fun getFavoriteTransfer() = networkService(30).getFavoriteTransfer()

    fun pinFavorite(request: PinFavoriteRequest) = networkService(30).pinFavorite(request)
}