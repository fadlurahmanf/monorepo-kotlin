package com.fadlurahmanf.bebas_onboarding.presentation

import android.view.View
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_onboarding.domain.di.BebasOnboardingComponent
import com.fadlurahmanf.bebas_onboarding.domain.di.DaggerBebasOnboardingComponent
import com.fadlurahmanf.bebas_storage.DaggerBebasStorageComponent
import com.fadlurahmanf.bebas_ui.presentation.activity.BaseBebasActivity
import com.fadlurahmanf.bebas_ui.presentation.activity.BebasInflateActivity
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseOnboardingActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseBebasActivity<VB>(inflate) {

    lateinit var component: BebasOnboardingComponent
    override fun initComponent() {
        val cryptoComponent = DaggerCoreCryptoComponent.factory().create()
        component = DaggerBebasOnboardingComponent.factory()
            .create(
                this,
                cryptoComponent,
                DaggerCorePlatformComponent.factory().create(),
                DaggerBebasStorageComponent.factory().create(this, cryptoComponent)
            )
    }
}