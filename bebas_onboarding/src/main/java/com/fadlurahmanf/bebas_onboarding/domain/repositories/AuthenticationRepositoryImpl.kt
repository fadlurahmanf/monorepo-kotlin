package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.IdentityGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.auth.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.auth.AuthResponse
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val identityGuestRemoteDatasource: IdentityGuestRemoteDatasource,
) {

    fun logConsole() = (context.applicationContext as BebasApplication).logConsole

    fun login(plainPassword: String): Observable<AuthResponse> {
        val request = LoginRequest(
            nik = "3511333333333333",
            deviceId = "3caf1726e8a74121440b2daf15ec0c0efa4f85530afc77aaa7629a35a07a4a2e",
            deviceToken = "fYwiUN5ARiGlnD0JnU7v8r:APA91bET5UbicvqceOlWl4HKvpomonIAxP1-VYcHMmYJ8fkuEw1DaNdH23YRXJoabonTeDx7M7TVWabrEUD90XNUnO1JReeqLqKPBuUE063I4FsLt1LSPw7heSMbM5n599ift_jbymE2",
            password = "HGKNJ9qJKYa6lwx4LeUx7g==",
            phoneNumber = "081283602321",
            activationId = "fbed3933-d36f-4ee8-800d-e245db8dd713"
        )
        return identityGuestRemoteDatasource.login(request).map {
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