package com.fadlurahmanf.bebas_onboarding.presentation.email

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val emailArg = intent.extras?.getString(EMAIL_ARG)

        if (emailArg == null) {
            showFailedBebasBottomsheet(BebasException.generalRC("EMAIL_MISSING"))
            return
        }

        binding.srl.setOnRefreshListener {
            viewModel.checkIsEmailVerify(email)
        }

        email = emailArg

        binding.etEmail.text = email
        binding.etEmail.setIsEnabled(false)

        initObserver()
        viewModel.requestEmail(email)
    }

    private fun initObserver() {
        viewModel.checkEmailVerifyState.observe(this) {
            when (it) {
                is CheckIsEmailVerifyState.IsVerified -> {
                    binding.srl.isRefreshing = false
                    setResult(Activity.RESULT_OK, intent.apply {
                        putExtra("EMAIL_TOKEN", it.emailToken)
                    })
                    finish()
                }

                is CheckIsEmailVerifyState.FAILED -> {
                    Log.d("BebasLogger", "TES FAILED: ${it.exception.toJson()}")
                    binding.srl.isRefreshing = false
                }

                else -> {}
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
                    showFailedBebasBottomsheet(it.exception)
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val uriToken = intent?.data?.getQueryParameter("token")
        if (uriToken != null) {
            handler.postDelayed({
                                    viewModel.checkIsEmailVerify(email)
                                }, 3000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}