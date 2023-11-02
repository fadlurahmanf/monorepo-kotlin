package com.fadlurahmanf.bebas_onboarding.presentation

import android.view.View
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_ui.presentation.activity.BaseBebasActivity
import com.fadlurahmanf.bebas_ui.presentation.activity.BebasInflateActivity

abstract class BaseOnboardingActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseBebasActivity<VB>(inflate) {

    override fun initComponent() {

    }
}