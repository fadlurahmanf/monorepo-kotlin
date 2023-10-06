package com.fadlurahmanf.mapp_example.presentation.session

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_example.R
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
            when(state){
                is NetworkState.LOADING -> {
                    Log.d("MappLogger", "MASUK LOADING")
                }

                is NetworkState.SUCCESS -> {
                    Log.d("MappLogger", "MASUK SUCCESS")
                }

                is NetworkState.FAILED -> {
                    Log.d("MappLogger", "MASUK FAILED: ${state.exception.toJson()}")
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