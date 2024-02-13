package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.data.datasources.IdentityGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.auth.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.auth.AuthResponse
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_fcm.domain.repositories.BebasFcmRepository
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepositoryImpl
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val bebasFcmRepository: BebasFcmRepository,
    private val cryptoAuthenticationRepositoryImpl: CryptoAuthenticationRepositoryImpl,
    private val deviceRepositoryImpl: DeviceRepository,
    private val identityGuestRemoteDatasource: IdentityGuestRemoteDatasource,
) {

    fun logConsole() = (context.applicationContext as BebasApplication).logConsole

    fun login(plainPassword: String): Observable<AuthResponse> {
        return bebasFcmRepository.getFcmToken().flatMap { fcmToken ->
            val encryptedPassword =
                cryptoAuthenticationRepositoryImpl.encryptPassword(password = plainPassword)
                    ?: throw BebasException.generalRC("ENC_PASS_00")
            Log.d("BebasLogger", "ENC_PASS: $encryptedPassword")
            val request = LoginRequest(
                nik = "3511222222222222",
                deviceId = deviceRepositoryImpl.deviceID(context),
                deviceToken = fcmToken,
                password = encryptedPassword,
                phoneNumber = "081283602320",
                activationId = "e562da9e-4e65-4472-9072-921d5215c10f"
            )
            identityGuestRemoteDatasource.login(request).map {
                if (it.data?.accessToken == null || it.data?.refreshToken == null) {
                    throw BebasException.generalRC("TOKEN_MISSING")
                }
                BebasShared.setAccessToken(it.data?.accessToken ?: "")
                BebasShared.setRefreshToken(it.data?.refreshToken ?: "")
                RxBus.publish(
                    RxEvent.ResetTimerForceLogout(
                        expiresIn = it.data?.expiresIn ?: (60L * 3),
                        refreshExpiresIn = it.data?.refreshExpiresIn ?: (60L * 5)
                    )
                )
                it.data!!
            }
        }
    }
}