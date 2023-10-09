package com.fadlurahmanf.mapp_splash.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_splash.DaggerMappSplashComponent
import com.fadlurahmanf.mapp_splash.MappSplashComponent
import com.fadlurahmanf.mapp_splash.R
import com.fadlurahmanf.mapp_splash.databinding.ActivitySplashBinding
import com.fadlurahmanf.mapp_splash.presentation.view_model.SplashViewModel
import javax.inject.Inject

class SplashActivity :
    BaseMappSplashActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun setup() {
        viewModel.guestToken.observe(this) {
            Log.d("MappLogger", "MASUK STATE: $it")
            when (it) {
                is NetworkState.SUCCESS -> {
                    val intent = Intent(
                        this,
                        Class.forName("com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                is NetworkState.FAILED -> {
                    Log.d("MappLogger", "MASUK STATE: ${it.exception.message}")
                    showBaseSplashFailedBottomSheet(it.exception)
                }

                else -> {

                }
            }
        }

        viewModel.generateGuestToken()
    }
}