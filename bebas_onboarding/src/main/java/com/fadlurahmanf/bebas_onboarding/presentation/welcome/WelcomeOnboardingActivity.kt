package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.viewpager2.widget.ViewPager2
import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.state.InitWelcomeState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityWelcomeOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.enum_class.OnboardingFlow
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
        initAction()
        adapter = BannerAdapter()
        adapter.setList(banners)
        binding.vp.adapter = adapter

        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                refreshIndicator(position)
            }
        })

        viewModel.lang.observe(this) {
            when (it) {
                "en-EN" -> {
                    binding.tvLangEn.visibility = View.VISIBLE
                    binding.tvLangId.visibility = View.INVISIBLE
                }

                else -> {
                    binding.tvLangEn.visibility = View.INVISIBLE
                    binding.tvLangId.visibility = View.VISIBLE
                }
            }
        }

        viewModel.state.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    banners.clear()
                    banners.addAll(it.data)
                    adapter.setList(banners)
                    createBanners()
                }

                else -> {

                }
            }
        }

        viewModel.initState.observe(this) {
            when (it) {
                is InitWelcomeState.SuccessToTnc -> {
                    val intent = Intent(this, TncActivity::class.java)
                    startActivity(intent)
                }

                else -> {}
            }
        }

        viewModel.getExistingLanguage()
        viewModel.getWelcomeBanner()
        viewModel.initLastStorage()
    }

    private lateinit var indicatorBanners: Array<ImageView?>
    private var currentIndex: Int = 0

    private fun createBanners() {
        indicatorBanners = arrayOfNulls<ImageView>(banners.size)
        for (i in indicatorBanners.indices) {
            if (i != currentIndex) {
                val view = ImageView(applicationContext)
                val lp = LinearLayout.LayoutParams(30, 30)
                lp.setMargins(10, 0, 10, 0)
                view.layoutParams = lp
                view.background =
                    ContextCompat.getDrawable(this, R.drawable.indicator_banner_inactive)
                indicatorBanners[i] = view
            } else {
                val view = ImageView(applicationContext)
                val lp = LinearLayout.LayoutParams(70, 30)
                lp.setMargins(10, 0, 10, 0)
                view.layoutParams = lp
                view.background =
                    ContextCompat.getDrawable(this, R.drawable.indicator_banner_active)
                indicatorBanners[i] = view
            }
        }

        binding.llIndicator.removeAllViews()

        for (element in indicatorBanners) {
            binding.llIndicator.addView(element)
        }
    }

    private fun refreshIndicator(currentIndex: Int) {
        for (i in 0 until binding.llIndicator.childCount) {
            if (i != currentIndex) {
                val view = binding.llIndicator.getChildAt(i) as ImageView
                val lp = LinearLayout.LayoutParams(30, 30)
                lp.setMargins(10, 0, 10, 0)
                view.layoutParams = lp
                view.background =
                    ContextCompat.getDrawable(this, R.drawable.indicator_banner_inactive)
            } else {
                val view = binding.llIndicator.getChildAt(i) as ImageView
                val lp = LinearLayout.LayoutParams(70, 30)
                lp.setMargins(10, 0, 10, 0)
                view.layoutParams = lp
                view.background =
                    ContextCompat.getDrawable(this, R.drawable.indicator_banner_active)
            }
        }
    }

    private fun initAction() {
        binding.toggleSwitch.setOnClickListener {
            viewModel.switchLanguage()
        }

        binding.btnCreateNewAccount.setOnClickListener {
            viewModel.updateOobFlow(OnboardingFlow.CREATE_ACCOUNT)
            val intent = Intent(this, TncActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginDiffAccount.setOnClickListener {
            viewModel.updateOobFlow(OnboardingFlow.ALREADY_HAVE_ACCOUNT_NUMBER)
            val intent = Intent(this, TncActivity::class.java)
            startActivity(intent)
        }
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