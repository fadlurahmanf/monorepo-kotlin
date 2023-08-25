package com.fadlurahmanf.mapp

import android.app.Application
import com.fadlurahmanf.mapp.di.DaggerMappDevComponent
import com.fadlurahmanf.mapp.di.MappComponent
import com.fadlurahmanf.mapp.di.MappDevComponent

class MappApplication : Application() {
    lateinit var component: MappComponent
    override fun onCreate() {
        super.onCreate()
        initInjection()
    }

    private fun initInjection() {
        when (BuildConfig.FLAVOR) {
            "dev" -> {
                component = DaggerMappDevComponent.factory().create(this)
            }
        }
        component.inject(this)
    }
}