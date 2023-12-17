package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OpenviduApi
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionRequest
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import com.fadlurahmanf.bebas_api.domain.network.OpenviduNetwork
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class OpenviduDatasource @Inject constructor(
    context: Context
) : OpenviduNetwork<OpenviduApi>(context) {
    override fun getApi(): Class<OpenviduApi> = OpenviduApi::class.java


    fun createSession(sessionId: String): Observable<ConnectionResponse> {
        val request = ConnectionRequest()
        return networkService().initializeConnection(
            sessionId = sessionId,
            body = request
        )
    }
}