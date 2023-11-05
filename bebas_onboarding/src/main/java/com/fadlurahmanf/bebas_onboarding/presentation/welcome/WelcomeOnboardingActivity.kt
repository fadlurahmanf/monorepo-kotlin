package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityWelcomeOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_ui.font.BebasFontTypeSpan
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
        initFooterText()
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

    private fun initFooterText() {
        val spannable1 = SpannableString("Klik disini untuk mengetahui Lokasi ")
        spannable1.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0,
            spannable1.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spannable2 = SpannableString("Cabang & ATM ")
        spannable2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_primary)),
            0,
            spannable2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable2.setSpan(
            BebasFontTypeSpan("", ResourcesCompat.getFont(this, R.font.lexend_deca_bold)!!),
            0,
            spannable2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spannable3 = SpannableString("klik ")
        spannable3.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0,
            spannable3.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spannable4 = SpannableString("Call Center ")
        spannable4.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_primary)),
            0,
            spannable4.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable4.setSpan(
            BebasFontTypeSpan("", ResourcesCompat.getFont(this, R.font.lexend_deca_bold)!!),
            0,
            spannable4.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val spannable5 = SpannableString("untuk menghubungi kami.")
        spannable5.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0,
            spannable5.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val spannableStringBuilder = buildSpannedString {
            append(spannable1)
            append(spannable2)
            append(spannable3)
            append(spannable4)
            append(spannable5)
        }

        binding.tvFooterBebas.text = spannableStringBuilder
    }
}