package com.fadlurahmanf.mapp_example.presentation

import android.os.CountDownTimer
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_ui.presentation.activity.BaseMappActivity
import com.fadlurahmanf.mapp_example.MappExampleComponent
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity
import java.util.Timer

abstract class BaseExampleActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseMappActivity<VB>(inflater) {
    lateinit var component: MappExampleComponent
    override fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(
                applicationContext,
                CoreInjectHelper.provideCoreCryptoComponent(applicationContext),
                CoreInjectHelper.provideCorePlatformComponent(applicationContext),
                CoreInjectHelper.provideMappConfigComponent(applicationContext),
                CoreInjectHelper.provideMappFcmComponent(applicationContext),
                CoreInjectHelper.provideMappFirebaseDatabaseComponent(applicationContext),
            )
    }
}

abstract class BaseAfterLoginExampleActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseExampleActivity<VB>(inflater) {

    private var lastUsed:Long = System.currentTimeMillis()
    override fun onUserInteraction() {
        super.onUserInteraction()
    }

    val timer = object : CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {

        }

        override fun onFinish() {

        }

    }
}

