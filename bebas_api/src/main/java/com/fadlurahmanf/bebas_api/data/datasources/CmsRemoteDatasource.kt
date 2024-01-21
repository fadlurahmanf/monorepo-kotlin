package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.CmsApi
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataRequest
import com.fadlurahmanf.bebas_api.domain.network.CmsNetwork
import javax.inject.Inject

class CmsRemoteDatasource @Inject constructor(
    context: Context
) :
    CmsNetwork<CmsApi>(context) {
    override fun getApi(): Class<CmsApi> = CmsApi::class.java

    fun getTransactionMenu() = networkService().getTransactionMenu()

    fun getBankList() = networkService().getBankList()
    fun getHomepagePromo() = networkService().getHomePagePromo()

    fun getHomepageBannerInfo() = networkService().getBannerInfo()

    fun inquiryPulsaData(request: InquiryPulsaDataRequest) =
        networkService().inquiryPulsaData(request)
}