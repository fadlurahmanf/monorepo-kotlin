package com.fadlurahmanf.bebas_onboarding.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.OpenviduDatasource
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class VideoCallRepository @Inject constructor(
    private val openviduDatasource: OpenviduDatasource
) {
    fun initializeConnection(sessionId: String): Observable<ConnectionResponse> {
        return openviduDatasource.createSession(sessionId)
    }
}