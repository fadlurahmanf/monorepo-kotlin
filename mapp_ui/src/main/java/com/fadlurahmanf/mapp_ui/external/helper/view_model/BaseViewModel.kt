package com.fadlurahmanf.mapp_ui.external.helper.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fadlurahmanf.mapp_shared.rxbus.RxBus
import com.fadlurahmanf.mapp_shared.rxbus.RxEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel:ViewModel() {
    fun compositeDisposable() = CompositeDisposable()
}

abstract class AfterLoginBaseViewModel:BaseViewModel(){

    init {

    }

    private fun listenAction(){
        compositeDisposable().add(RxBus.listen(RxEvent.CheckUserTokenExpiresAt::class.java).subscribe {
            Log.d("MappLogger", "RECEIVED ACTION ${RxEvent.CheckUserTokenExpiresAt::class.java.simpleName}")
            compositeDisposable().clear()
        })
    }
}