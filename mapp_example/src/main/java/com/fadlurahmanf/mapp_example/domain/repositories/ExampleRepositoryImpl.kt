package com.fadlurahmanf.mapp_example.domain.repositories

import android.annotation.SuppressLint
import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityGuestTokenRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.data.dto.identity.AuthResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val masIdentityGuestTokenRemoteDatasource: MasIdentityGuestTokenRemoteDatasource,
    private val mappLocalDatasource: MappLocalDatasource
) {
    @SuppressLint("CheckResult")
    fun login(): Observable<BaseResponse<AuthResponse>> {
        val loginRequest = LoginRequest(
            nik = "3511222222222222",
            deviceId = "05242c96c62d2351",
            deviceToken = "TES_TOKEN_PALSU",
            password = "ez1DcqL8iatoZequkUvP1A==",
            phoneNumber = "081283602320",
            activationId = "f7db6aea-a197-46e3-8bac-4071cfd0e568",
            timestamp = System.currentTimeMillis().toString()
        )
        return masIdentityGuestTokenRemoteDatasource.login(loginRequest).doOnNext { result ->
            result.data?.expiresIn
            if (result.data == null) {
                throw MappException.generalRC("DATA_EMPTY")
            }

            if (result.data?.accessToken?.isEmpty() == true || result.data?.refreshToken?.isEmpty() == true) {
                throw MappException.generalRC("TOKEN_MISSING")
            }

            mappLocalDatasource.getAll().map { entities ->
                if (entities.isEmpty()) {
                    throw MappException.generalRC("ENTITIES_EMPTY")
                }
                val entity = entities.first().copy(
                    accessToken = result.data?.accessToken,
                    refreshToken = result.data?.refreshToken,
                    expiresIn = result.data?.expiresIn,
                    refreshExpiresIn = result.data?.refreshExpiresIn
                )
                mappLocalDatasource.update(entity)
            }
        }
    }

    fun refreshUserToken(): Observable<BaseResponse<AuthResponse>> {
        return mappLocalDatasource.getAll().toObservable().flatMap { entities ->
            if (entities.isEmpty()) {
                throw MappException.generalRC("ENTITY_EMPTY")
            }

            val entity = entities.first()

            if (entity.refreshToken == null) {
                throw MappException.generalRC("REFRESH_TOKEN_MISSING")
            }

            val refreshToken = entity.refreshToken!!

            val request = RefreshUserTokenRequest(
                token = refreshToken
            )

            masIdentityGuestTokenRemoteDatasource.refreshToken(request).doOnNext { result ->
                if (result.data == null) {
                    throw MappException.generalRC("DATA_MISSING")
                }

                val authResponse = result.data!!

                mappLocalDatasource.update(
                    value = entity.copy(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken,
                        expiresIn = authResponse.expiresIn,
                        refreshExpiresIn = authResponse.refreshExpiresIn
                    )
                )
            }
        }
    }
}