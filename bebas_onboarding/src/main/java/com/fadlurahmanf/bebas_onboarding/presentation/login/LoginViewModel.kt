package com.fadlurahmanf.bebas_onboarding.presentation.login

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.auth.AuthResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.AuthenticationRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executor
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authenticationRepositoryImpl: AuthenticationRepositoryImpl,
    private val deviceRepository: DeviceRepository,
) : BaseViewModel() {

    fun authenticateBiometric(
        context: Context,
        fragmentActivity: FragmentActivity,
        executor: Executor,
        callback: CoreBiometric.AuthenticateGeneralCallback
    ) {
        if (deviceRepository.isSupportedBiometric(context)) {
            deviceRepository.authenticateGeneral(
                context, fragmentActivity, executor,
                titleText = "Login",
                descriptionText = "Login menggunakan biometric",
                negativeText = "Batal",
                callback = callback
            )
        }
    }

    private val _loginState = MutableLiveData<NetworkState<AuthResponse>>()
    val loginState: LiveData<NetworkState<AuthResponse>> = _loginState

    fun login() {
        _loginState.value = NetworkState.LOADING
        baseDisposable.add(authenticationRepositoryImpl.login(
            plainPassword = "Abcd1234"
        ).subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _loginState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _loginState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}