package com.fadlurahmanf.mapp_example.presentation.session

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        viewModel.loginState.observe(this) {
            Log.d("MappLogger", "MASUK LOGIN STATE: $it")
        }

        binding.btnLogin.onClicked {
            viewModel.login()
        }
    }
}