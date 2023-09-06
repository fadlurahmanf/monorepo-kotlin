package com.fadlurahmanf.mapp_splash.presentation.view_model

import com.fadlurahmanf.mapp_splash.domain.repositories.SplashRepositoryImpl
import com.fadlurahmanf.mapp_ui.presentation.view_model.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repositoryImpl: SplashRepositoryImpl
) : BaseViewModel() {
    fun generateGuestToken() {
        try {
            compositeDisposable().add(
                repositoryImpl.generateGuestToken(
                    CreateGuestTokenRequest(
                        guestId = UUID.randomUUID().toString()
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {

                        },
                        {

                        },
                        {

                        },
                    )
            )
        } catch (e: Exception) {

        }
    }
}