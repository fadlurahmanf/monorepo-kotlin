package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.content.Intent
import android.text.Html
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.state.InitTncState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityTncBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.InputPhoneEmailActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import javax.inject.Inject

class TncActivity : BaseOnboardingActivity<ActivityTncBinding>(ActivityTncBinding::inflate) {

    @Inject
    lateinit var viewModel: TncViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchIsTncRead(isChecked)
        }

        binding.llScrollToBottom.setOnClickListener {
            binding.scrollView.fullScroll(View.FOCUS_DOWN)
            binding.appbarLayout.setExpanded(false)
            viewModel.switchIsTncRead(true)
        }

        binding.btnNext.setOnClickListener {
            viewModel.updateIsFinishedReadTnc(true)
            val intent = Intent(this, InputPhoneEmailActivity::class.java)
            startActivity(intent)
        }

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isReachedBottom(scrollY)) {
                binding.llScrollToBottom.visibility = View.GONE
                binding.checkbox.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                binding.llScrollToBottom.visibility = View.VISIBLE
            }
        })

        viewModel.initState.observe(this) {
            when (it) {
                is InitTncState.SuccessToInputPhoneAndEmail -> {
                    binding.llScrollToBottom.visibility = View.GONE
                    binding.checkbox.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.checkbox.isChecked = true
                    val intent = Intent(this, InputPhoneEmailActivity::class.java)
                    startActivity(intent)
                }

                else -> {

                }
            }
        }

        viewModel.isTncRead.observe(this) {
            when (it) {
                true -> {
                    binding.btnNext.setActive(true)
                }

                else -> {
                    binding.btnNext.setActive(false)
                }
            }
        }

        viewModel.tncContentState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    binding.tvText.text = Html.fromHtml(it.data.text ?: "")
                    binding.tvText.visibility = View.VISIBLE
                    binding.tncShimmer.root.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.tvText.visibility = View.GONE
                    binding.tncShimmer.root.visibility = View.VISIBLE
                }

                else -> {}
            }
        }


        viewModel.getTNC()
        viewModel.initState()
    }

    private fun isReachedBottom(scrollY: Int): Boolean {
        val view = binding.scrollView.getChildAt(binding.scrollView.childCount - 1)
        val bottomDetector: Int = view.bottom - (binding.scrollView.height + scrollY)
        return bottomDetector <= 300
    }

    override fun onDestroy() {
        viewModel.updateIsFinishedReadTnc(false)
        viewModel.removeOnboardingFlow()
        super.onDestroy()
    }
}