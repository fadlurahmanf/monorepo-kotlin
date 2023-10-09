package com.fadlurahmanf.mapp_example.presentation

import android.util.Log
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_shared.rxbus.RxBus
import com.fadlurahmanf.mapp_shared.rxbus.RxEvent
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity

abstract class BaseExampleActivityAfterLogin<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseExampleActivity<VB>(inflater) {

    private var lastUsed:Long = System.currentTimeMillis()
    override fun onUserInteraction() {
        super.onUserInteraction()
        Log.d("MappLogger", "onUserInteraction")
        RxBus.publish(RxEvent.CheckUserTokenExpiresAt())
    }
}