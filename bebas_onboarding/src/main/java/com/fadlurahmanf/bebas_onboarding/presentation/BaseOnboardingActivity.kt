package com.fadlurahmanf.bebas_onboarding.presentation

import android.view.View
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_onboarding.domain.di.BebasOnboardingComponent
import com.fadlurahmanf.bebas_onboarding.domain.di.DaggerBebasOnboardingComponent
import com.fadlurahmanf.bebas_ui.presentation.activity.BaseBebasActivity
import com.fadlurahmanf.bebas_ui.presentation.activity.BebasInflateActivity

abstract class BaseOnboardingActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseBebasActivity<VB>(inflate) {

    lateinit var component: BebasOnboardingComponent
    override fun initComponent() {
        component = DaggerBebasOnboardingComponent.factory().create(this)
    }
}