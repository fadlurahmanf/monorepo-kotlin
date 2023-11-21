package com.fadlurahmanf.bebas_onboarding.presentation.email

import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.state.CheckIsEmailVerifyState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEmailVerificationBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import java.util.Timer
import javax.inject.Inject

class EmailVerificationActivity :
    BaseOnboardingActivity<ActivityEmailVerificationBinding>(ActivityEmailVerificationBinding::inflate) {

    @Inject
    lateinit var viewModel: EmailVerificationViewModel

    companion object {
        const val EMAIL_ARG = "EMAIL_ARG"
    }

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var email: String

    override fun setup() {
        val emailArg = intent.extras?.getString(EMAIL_ARG)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (emailArg == null) {
            showFailedBottomsheet(BebasException.generalRC("EMAIL_MISSING"))
            return
        }

        email = emailArg

        binding.etEmail.text = email
        binding.etEmail.setIsEnabled(false)

        initObserver()
        fetchStatusEmail()
        viewModel.requestEmail(email)
    }

    private fun initObserver() {
        viewModel.checkEmailVerifyState.observe(this) {
            when (it) {
                CheckIsEmailVerifyState.IsVerified -> {
                    handler.removeCallbacks(fetchEmailStatusRunnable)
                }

                else -> {
                    handler.postDelayed(fetchEmailStatusRunnable, 10000)
                }
            }
        }

        viewModel.requestEmailState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    if (it.data.remainingEmailInSecond > 0) {
                        setTimer(it.data.remainingEmailInSecond)
                    }
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                else -> {

                }
            }
        }
    }

    private var timer: CountDownTimer? = null

    fun setTimer(remainInSecond: Long) {
        timer = object : CountDownTimer(remainInSecond * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnCounterEmailRetry.visibility = View.GONE
                binding.tvCountdownEmailRetry.visibility = View.VISIBLE
                binding.tvCountdownEmailRetry.text =
                    getString(R.string.resend_email_in_seconds, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.btnCounterEmailRetry.visibility = View.VISIBLE
                binding.tvCountdownEmailRetry.visibility = View.GONE
            }
        }
        timer?.start()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val fetchEmailStatusRunnable = object : Runnable {
        override fun run() {
            viewModel.checkIsEmailVerify(email)
            handler.removeCallbacks(this)
        }

    }

    private fun fetchStatusEmail() {
        handler.postDelayed(fetchEmailStatusRunnable, 3000)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uriToken = intent?.data?.getQueryParameter("token")
        if (uriToken != null) {
            handler.removeCallbacks(fetchEmailStatusRunnable)
//            viewModel.checkIsEmailVerify()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(fetchEmailStatusRunnable)
        super.onDestroy()
    }

}