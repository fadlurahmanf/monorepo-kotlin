package com.fadlurahmanf.mapp_example.presentation

import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.core_platform.domain.BiometricRepositoryImpl
import com.fadlurahmanf.mapp_example.R
import javax.inject.Inject

class ExampleActivity : BaseExampleActivity() {
    override fun setup() {
        setContentView(R.layout.activity_example)

        // todo
        // get biometric here
        println("MASUK TES ${biometricRepositoryImpl.isSupportedBiometric(this)}")
    }

    override fun injectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var biometricRepositoryImpl: BiometricRepository
}