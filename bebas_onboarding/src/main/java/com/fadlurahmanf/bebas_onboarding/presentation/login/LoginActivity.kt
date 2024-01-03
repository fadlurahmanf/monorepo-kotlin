package com.fadlurahmanf.bebas_onboarding.presentation.login

import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityLoginBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric
import javax.inject.Inject

class LoginActivity : BaseOnboardingActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        viewModel.loginState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    val intent = Intent(
                        this,
                        Class.forName("com.fadlurahmanf.bebas_main.presentation.home.HomeActivity")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                else -> {

                }
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        handler.postDelayed({
                                viewModel.authenticateBiometric(
                                    applicationContext,
                                    fragmentActivity = this,
                                    executor = ContextCompat.getMainExecutor(applicationContext),
                                    callback = object : CoreBiometric.AuthenticateGeneralCallback {
                                        override fun onAuthenticationSuccess(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                                            viewModel.login()
                                        }

                                        override fun onAuthenticationFailed() {

                                        }

                                        override fun onAuthenticationError(
                                            errorCode: Int,
                                            errString: CharSequence?
                                        ) {

                                        }


                                    }
                                )
                            }, 1000)
    }
}