package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityWelcomeOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import javax.inject.Inject

class WelcomeOnboardingActivity :
    BaseOnboardingActivity<ActivityWelcomeOnboardingBinding>(ActivityWelcomeOnboardingBinding::inflate) {

    @Inject
    lateinit var viewModel: WelcomeOnboardingViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: BannerAdapter
    private val banners: ArrayList<WelcomeBannerResponse> = arrayListOf()

    override fun setup() {
        adapter = BannerAdapter()
        adapter.setList(banners)
        binding.vp.adapter = adapter

        viewModel.state.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    banners.clear()
                    banners.addAll(it.data)
                    adapter.setList(banners)
                }

                else -> {

                }
            }
        }

        viewModel.getWelcomeBanner()
    }
}