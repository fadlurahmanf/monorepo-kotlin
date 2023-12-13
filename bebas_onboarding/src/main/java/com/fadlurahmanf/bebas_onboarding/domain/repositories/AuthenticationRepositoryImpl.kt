package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.IdentityGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.auth.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.auth.LoginResponse
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val identityGuestRemoteDatasource: IdentityGuestRemoteDatasource,
) {

    fun logConsole() = (context.applicationContext as BebasApplication).logConsole

    fun login(password: String): Observable<LoginResponse> {
        val request = LoginRequest(
            nik = "3511222222222222",
            deviceId = "3caf1726e8a74121440b2daf15ec0c0efa4f85530afc77aaa7629a35a07a4a2e",
            deviceToken = "fYwiUN5ARiGlnD0JnU7v8r:APA91bET5UbicvqceOlWl4HKvpomonIAxP1-VYcHMmYJ8fkuEw1DaNdH23YRXJoabonTeDx7M7TVWabrEUD90XNUnO1JReeqLqKPBuUE063I4FsLt1LSPw7heSMbM5n599ift_jbymE2",
            password = password,
            phoneNumber = "081283602320",
            activationId = "45fa5456-e765-4976-ae1f-639f3cd6c633"
        )
        return identityGuestRemoteDatasource.login(request).map {
            if (it.data?.accessToken == null || it.data?.refreshToken == null) {
                throw BebasException.generalRC("TOKEN_MISSING")
            }
            it.data!!
        }
    }
}