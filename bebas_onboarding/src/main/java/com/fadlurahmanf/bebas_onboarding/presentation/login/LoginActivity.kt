package com.fadlurahmanf.bebas_onboarding.presentation.login

import android.content.Intent
import android.os.Bundle
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityLoginBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import javax.inject.Inject

class LoginActivity : BaseOnboardingActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun injectActivity() {
        component.inject(this)
    }

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
                        Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                    )
                    startActivity(intent)
                }

                else -> {

                }
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }

        // fake
        viewModel.login()
    }
}