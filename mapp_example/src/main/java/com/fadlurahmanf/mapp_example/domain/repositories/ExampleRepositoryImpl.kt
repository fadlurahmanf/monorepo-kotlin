package com.fadlurahmanf.mapp_example.domain.repositories

import android.util.Log
import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityGuestTokenRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.data.dto.identity.AuthResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_shared.extension.*
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import io.reactivex.rxjava3.core.Observable
import java.util.Calendar
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val masIdentityGuestTokenRemoteDatasource: MasIdentityGuestTokenRemoteDatasource,
    private val mappLocalDatasource: MappLocalDatasource
) {
    fun login(): Observable<Boolean> {
        val loginRequest = LoginRequest(
            nik = "3511222222222222",
            deviceId = "05242c96c62d2351",
            deviceToken = "TES_TOKEN_PALSU",
            password = "ez1DcqL8iatoZequkUvP1A==",
            phoneNumber = "081283602320",
            activationId = "f7db6aea-a197-46e3-8bac-4071cfd0e568",
            timestamp = System.currentTimeMillis().toString()
        )
        return masIdentityGuestTokenRemoteDatasource.login(loginRequest).flatMap { result ->
            if (result.data == null) {
                throw MappException.generalRC("DATA_EMPTY")
            }

            if (result.data?.accessToken?.isEmpty() == true || result.data?.refreshToken?.isEmpty() == true) {
                throw MappException.generalRC("TOKEN_MISSING")
            }

            mappLocalDatasource.getAll().toObservable().flatMap { entities ->
                if (entities.isEmpty()) {
                    throw MappException.generalRC("ENTITIES_EMPTY")
                }

                val expiresAt = Calendar.getInstance()
                expiresAt.add(Calendar.SECOND, 180)
                val refreshExpiresAt = Calendar.getInstance()
                refreshExpiresAt.add(Calendar.SECOND, 300)

                val entity = entities.first().copy(
                    accessToken = result.data?.accessToken,
                    refreshToken = result.data?.refreshToken,
                    expiresAt = expiresAt.time.formatDate5(),
                    refreshExpiresAt = refreshExpiresAt.time.formatDate5()
                )
                mappLocalDatasource.update(entity)
                Observable.just(true)
            }
        }
    }

    fun refreshUserToken(): Observable<Boolean> {
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

            masIdentityGuestTokenRemoteDatasource.refreshToken(request).flatMap { result ->
                if (result.data == null) {
                    throw MappException.generalRC("DATA_MISSING")
                }

                val authResponse = result.data!!

                val expiresAt = Calendar.getInstance()
                expiresAt.add(Calendar.SECOND, 180)
                val refreshExpiresAt = Calendar.getInstance()
                refreshExpiresAt.add(Calendar.SECOND, 300)

                mappLocalDatasource.update(
                    value = entity.copy(
                        accessToken = authResponse.accessToken,
                        refreshToken = authResponse.refreshToken,
                        expiresAt = expiresAt.time.formatDate5(),
                        refreshExpiresAt = refreshExpiresAt.time.formatDate5()
                    )
                )

                Observable.just(true)
            }
        }
    }
}