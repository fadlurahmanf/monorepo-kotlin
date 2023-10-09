package com.fadlurahmanf.mapp_example.presentation.session.view_model

import android.annotation.SuppressLint
import android.util.Log
import com.fadlurahmanf.mapp_example.domain.repositories.ExampleRepositoryImpl
import com.fadlurahmanf.mapp_shared.rxbus.RxBus
import com.fadlurahmanf.mapp_shared.rxbus.RxEvent
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import com.fadlurahmanf.mapp_ui.external.helper.view_model.BaseViewModel
import java.text.SimpleDateFormat

abstract class AfterLoginBaseViewModel(
    private val exampleRepositoryImpl: ExampleRepositoryImpl,
    private val mappLocalDatasource: MappLocalDatasource,
) :
    BaseViewModel() {

    init {
        listenAction()
    }

    private fun refreshUserToken() {
        compositeDisposable().add(exampleRepositoryImpl.refreshUserToken().subscribe(
            {
                Log.d("MappLogger", "Success Refresh Token")
            },
            {

            },
            {}
        ))
    }

    private fun forceLogout() {

    }

    private fun listenAction() {
        compositeDisposable().addAll(
            RxBus.listen(RxEvent.CheckUserTokenExpiresAt::class.java).subscribe {
                Log.d(
                    "MappLogger",
                    "RECEIVED ACTION ${RxEvent.CheckUserTokenExpiresAt::class.java.simpleName}"
                )

                val obs = mappLocalDatasource.getAll().toObservable().doOnNext { entities ->
                    Log.d("MappLogger", "entities length: ${entities.size}")
                    if (entities.isEmpty()) {
                        return@doOnNext
                    }

                    val entity = entities.first()

                    val stringRefreshExpiresAt = entity.refreshExpiresAt

                    val refreshExpiresAt =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stringRefreshExpiresAt)?.time

                    if (refreshExpiresAt == null) {
                        forceLogout()
                        return@doOnNext
                    }

                    var now = System.currentTimeMillis()

                    var dif = refreshExpiresAt - now

                    if (dif <= 10) {
                        forceLogout()
                        return@doOnNext
                    }

                    val stringExpiresAt = entity.expiresAt

                    Log.d("MappLogger", "stringExpiresAt: $stringExpiresAt")

                    if (stringExpiresAt.isNullOrEmpty()) {
                        return@doOnNext
                    }

                    val expiresAt =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stringExpiresAt)?.time

                    Log.d("MappLogger", "expiresAt: $expiresAt")

                    if (expiresAt == null) {
                        return@doOnNext
                    }

                    now = System.currentTimeMillis()

                    Log.d("MappLogger", "expiresAt: $expiresAt")
                    Log.d("MappLogger", "now: $now")

                    dif = expiresAt - now
                    Log.d("MappLogger", "diff: $dif")

                    // in millisecond
                    if (dif <= 30000) {
                        refreshUserToken()
                        return@doOnNext
                    }
                }

                obs.subscribe()
            },
        )
    }
}