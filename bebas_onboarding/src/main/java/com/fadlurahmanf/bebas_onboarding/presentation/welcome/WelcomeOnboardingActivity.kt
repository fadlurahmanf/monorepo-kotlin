package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.flow.onboarding.OnboardingFlow
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

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        setButtonText()
        setFooterText()
        initAction()
        adapter = BannerAdapter()
        adapter.setLanguage(BebasShared.language)
        adapter.setList(banners)
        binding.vp.adapter = adapter

        binding.vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                refreshIndicator(position)
            }
        })

        initObserver()

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

    private fun initObserver() {
        viewModel.lang.observe(this) {}

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
    }

    private fun initAction() {
        binding.toggleSwitch.setOnClickListener {
            viewModel.switchLanguage()
        }

        binding.btnCreateNewAccount.setOnClickListener {
            viewModel.updateOobFlow(OnboardingFlow.ONBOARDING)
            val intent = Intent(this, TncActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginDiffAccount.setOnClickListener {
            viewModel.updateOobFlow(OnboardingFlow.SELF_ACTIVATION)
            val intent = Intent(this, TncActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFooterText() {
        if (BebasShared.language == "en-US") {
            val spannable1 = SpannableString("Click here to find out the ")
            spannable1.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
                0,
                spannable1.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val spannable2 = SpannableString("Branches & ATM's ")
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
            val clickableSpannable2: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://bankmas.co.id/id/jaringan-kami/kantor-cabang/")
                    )
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            spannable2.setSpan(
                clickableSpannable2,
                0,
                spannable2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val spannable3 = SpannableString("location\nklik ")
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
            val clickableSpannable4: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:1500011")
                    )
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            spannable4.setSpan(
                clickableSpannable4,
                0,
                spannable4.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val spannable5 = SpannableString("to contact us.")
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
            binding.tvFooterBebas.movementMethod = LinkMovementMethod.getInstance()
            binding.tvFooterBebas.highlightColor = Color.TRANSPARENT
        } else {
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
            val clickableSpannable2: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://bankmas.co.id/id/jaringan-kami/kantor-cabang/")
                    )
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            spannable2.setSpan(
                clickableSpannable2,
                0,
                spannable2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            val spannable3 = SpannableString("\nklik ")
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
            val clickableSpannable4: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:1500011")
                    )
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            spannable4.setSpan(
                clickableSpannable4,
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
            binding.tvFooterBebas.movementMethod = LinkMovementMethod.getInstance()
            binding.tvFooterBebas.highlightColor = Color.TRANSPARENT
        }
    }

    override fun onChangeLanguageEvent(languageCode: String, countryCode: String) {
        super.onChangeLanguageEvent(languageCode, countryCode)
        Log.d("BebasLogger", "onChangeLanguageEvent: $languageCode & $countryCode")
        when ("$languageCode-$countryCode") {
            "en-US" -> {
                binding.tvLangEn.visibility = View.VISIBLE
                binding.tvLangId.visibility = View.INVISIBLE
            }

            else -> {
                binding.tvLangEn.visibility = View.INVISIBLE
                binding.tvLangId.visibility = View.VISIBLE
            }
        }
        resetTextAfterChangeLanguage()
    }

    private fun setButtonText() {
        binding.btnCreateNewAccount.setButtonText(getString(R.string.create_new_account_number))
        binding.btnLoginDiffAccount.setButtonText(getString(R.string.already_have_an_account_number))
    }

    private fun resetTextAfterChangeLanguage() {
        setButtonText()
        setFooterText()

        adapter.setLanguage(BebasShared.language)
    }
}