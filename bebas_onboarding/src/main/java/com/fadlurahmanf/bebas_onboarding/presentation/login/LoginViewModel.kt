package com.fadlurahmanf.bebas_onboarding.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.auth.AuthResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.AuthenticationRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authenticationRepositoryImpl: AuthenticationRepositoryImpl
) : BaseViewModel() {

    private val _loginState = MutableLiveData<NetworkState<AuthResponse>>()
    val loginState: LiveData<NetworkState<AuthResponse>> = _loginState

    fun login() {
        _loginState.value = NetworkState.LOADING
        baseDisposable.add(authenticationRepositoryImpl.login(
            plainPassword = "FuQZjObKaZMqBlBdWSQ/Ag=="
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