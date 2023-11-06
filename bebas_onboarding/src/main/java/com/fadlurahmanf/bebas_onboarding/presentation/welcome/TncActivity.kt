package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.text.Html
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
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
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchIsTncRead(isChecked)
        }

        binding.llScrollToBottom.setOnClickListener {
            binding.scrollView.fullScroll(View.FOCUS_DOWN)
            binding.appbarLayout.setExpanded(false)
            viewModel.switchIsTncRead(true)
        }

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isReachedBottom(scrollY)) {
                binding.llScrollToBottom.visibility = View.GONE
                binding.checkbox.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                binding.llScrollToBottom.visibility = View.VISIBLE
            }
        })

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

    private fun isReachedBottom(scrollY: Int): Boolean {
        val view = binding.scrollView.getChildAt(binding.scrollView.childCount - 1)
        val bottomDetector: Int = view.bottom - (binding.scrollView.height + scrollY)
        return bottomDetector <= 300
    }
}