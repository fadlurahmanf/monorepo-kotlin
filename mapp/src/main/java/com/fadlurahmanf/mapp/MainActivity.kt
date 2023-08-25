package com.fadlurahmanf.mapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.core_platform.domain.BiometricRepositoryImpl
import com.fadlurahmanf.mapp.di.MappComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var mappComponent:MappComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mappComponent = (applicationContext as MappApplication).component
        mappComponent.example().create().inject(this)

        println("MASUK MASUK ${repository.isSupportedBiometric(this)}")
    }

    @Inject
    lateinit var repository: BiometricRepository
}