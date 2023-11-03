package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.text.Html
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityTnc2Binding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import javax.inject.Inject

class TncActivity : BaseOnboardingActivity<ActivityTnc2Binding>(ActivityTnc2Binding::inflate) {

    @Inject
    lateinit var viewModel: TncViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        viewModel.state.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    binding.tvText.text = Html.fromHtml(it.data.text ?: "")
                }

                else -> {}
            }
        }


        viewModel.getTNC()
    }
}