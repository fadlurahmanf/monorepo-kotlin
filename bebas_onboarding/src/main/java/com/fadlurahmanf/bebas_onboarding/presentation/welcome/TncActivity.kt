package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.text.Html
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityTncBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import javax.inject.Inject

class TncActivity : BaseOnboardingActivity<ActivityTncBinding>(ActivityTncBinding::inflate) {

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