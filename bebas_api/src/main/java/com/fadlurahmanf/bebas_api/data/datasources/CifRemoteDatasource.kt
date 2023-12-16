package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.CifApi
import com.fadlurahmanf.bebas_api.data.api.CmsApi
import com.fadlurahmanf.bebas_api.domain.network.CifNetwork
import com.fadlurahmanf.bebas_api.domain.network.CmsNetwork
import javax.inject.Inject

class CifRemoteDatasource @Inject constructor(
    context: Context
) : CifNetwork<CifApi>(context) {
    override fun getApi(): Class<CifApi> = CifApi::class.java
}