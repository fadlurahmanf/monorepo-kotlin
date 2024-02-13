package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionRequest
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface OpenviduApi {
    @POST("openvidu/api/sessions/{sessionId}/connection")
    fun initializeConnection(
        @Path("sessionId") sessionId: String,
        @Body body: ConnectionRequest
    ): Observable<ConnectionResponse>
}