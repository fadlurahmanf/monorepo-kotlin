package com.fadlurahmanf.mapp_example.presentation.session.view_model

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.mapp_api.data.dto.identity.AuthResponse
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_example.domain.repositories.ExampleRepositoryImpl
import com.fadlurahmanf.mapp_ui.external.helper.view_model.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val exampleRepository: ExampleRepositoryImpl
) : BaseViewModel() {

    private val _loginState = MutableLiveData<NetworkState<Boolean>>()
    val loginState = _loginState

    fun login() {
        _loginState.value = NetworkState.LOADING
        compositeDisposable().add(
            exampleRepository.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _loginState.value = NetworkState.SUCCESS(it)
                    },
                    {
                        _loginState.value = NetworkState.FAILED(MappException.fromThrowable(it))
                    },
                    {}
                )
        )
    }
}