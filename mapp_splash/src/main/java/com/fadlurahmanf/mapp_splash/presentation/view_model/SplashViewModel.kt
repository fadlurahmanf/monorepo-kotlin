package com.fadlurahmanf.mapp_splash.presentation.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.mapp_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_splash.domain.repositories.SplashRepositoryImpl
import com.fadlurahmanf.mapp_ui.external.helper.view_model.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repositoryImpl: SplashRepositoryImpl
) : BaseViewModel() {

    private val _guestToken = MutableLiveData<NetworkState<CreateGuestTokenResponse>>()
    val guestToken = _guestToken
    fun generateGuestToken() {
        _guestToken.value = NetworkState.LOADING
        compositeDisposable().add(
            repositoryImpl.generateGuestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _guestToken.value = NetworkState.SUCCESS(it)
                    },
                    {
                        if (it is MappException) {
                            _guestToken.value = NetworkState.FAILED(it)
                        } else {
                            _guestToken.value =
                                NetworkState.FAILED(MappException(rawMessage = it.message))
                        }
                    },
                    {

                    },
                )
        )
    }
}