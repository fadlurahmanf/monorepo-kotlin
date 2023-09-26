package com.fadlurahmanf.mapp_splash.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            println("MASUK STATE: $it")
            when (it) {
                is NetworkState.SUCCESS -> {
                    val intent = Intent(
                        this,
                        Class.forName("com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity")
                    )
                    startActivity(intent)
                }

                is NetworkState.FAILED -> {
                    showBaseSplashFailedBottomSheet(it.exception)
                }

                else -> {

                }
            }
        }

        viewModel.generateGuestToken()
    }
}