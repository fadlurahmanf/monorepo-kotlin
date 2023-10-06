package com.fadlurahmanf.mapp_splash.domain.repositories

import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_splash.data.exception.SplashException
import com.fadlurahmanf.mapp_storage.data.entity.MappEntity
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import io.reactivex.rxjava3.core.Observable
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val identityDatasource: MasIdentityRemoteDatasource,
    private val mappLocalDatasource: MappLocalDatasource
) {
    fun generateGuestToken(): Observable<CreateGuestTokenResponse> {
        val guestTokenRequest = CreateGuestTokenRequest(
            guestId = UUID.randomUUID().toString()
        )
        var entities: List<MappEntity> = listOf()
        return mappLocalDatasource.getAll().toObservable().doOnNext { localData ->
            entities = localData
        }.flatMap { _ ->
            identityDatasource.generateGuestToken(guestTokenRequest).doOnNext { respToken ->
                if (respToken.data != null) {
                    if (respToken.data?.accessToken != null) {
                        val accessToken = respToken.data!!.accessToken!!
                        if (entities.isNotEmpty()) {
                            val entity = entities.first()
                            mappLocalDatasource.update(entity.copy(guestToken = accessToken))
                        } else {
                            val entity = MappEntity(
                                deviceId = "deviceId",
                                guestToken = accessToken
                            )
                            mappLocalDatasource.insert(entity)
                        }
                    } else {
                        throw MappException.generalRC("TOKEN_MISSING")
                    }
                } else {
                    throw MappException.generalRC("DATA_MISSING")
                }
            }.map {
                it.data!!
            }
        }
    }
}