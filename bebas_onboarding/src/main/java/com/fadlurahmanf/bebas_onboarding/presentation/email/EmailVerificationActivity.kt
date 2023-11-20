package com.fadlurahmanf.bebas_onboarding.presentation.email

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
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