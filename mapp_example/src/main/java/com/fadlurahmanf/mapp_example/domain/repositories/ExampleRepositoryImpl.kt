package com.fadlurahmanf.mapp_example.domain.repositories

import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityGuestTokenRemoteDatasource
import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginResponse
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val masIdentityGuestTokenRemoteDatasource: MasIdentityGuestTokenRemoteDatasource,
    private val mapLocalDatasource: MappLocalDatasource
) {
    fun login(): Observable<BaseResponse<LoginResponse>> {
        val loginRequest = LoginRequest(
            nik = "3511222222222222",
            deviceId = "05242c96c62d2351",
            password = "ez1DcqL8iatoZequkUvP1A==",
            phoneNumber = "081283602320",
            timestamp = System.currentTimeMillis().toString()
        )
        return masIdentityGuestTokenRemoteDatasource.login(loginRequest).doOnNext {

        }
    }
}