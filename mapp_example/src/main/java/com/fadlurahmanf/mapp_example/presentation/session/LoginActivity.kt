package com.fadlurahmanf.mapp_example.presentation.session

import android.content.Intent
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_example.databinding.ActivityLoginBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.session.view_model.LoginViewModel
import javax.inject.Inject

class LoginActivity : BaseExampleActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is NetworkState.LOADING -> {
                    showLottieLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    val intent = Intent(this, ActivityAfterLogin::class.java)
                    startActivity(intent)
                }

                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(
                        title = state.exception.toProperTitle(this),
                        desc = state.exception.toProperMessage(this),
                        buttonText = state.exception.toProperButtonText(this)
                    )
                }

                else -> {

                }
            }

        }

        binding.btnLogin.onClicked {
            viewModel.login()
        }
    }
}