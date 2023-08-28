package com.fadlurahmanf.mapp_example.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_example.MappExampleComponent

abstract class BaseExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initComponent()
        injectActivity()
        super.onCreate(savedInstanceState)
        setup()
    }

    abstract fun injectActivity()

    lateinit var component: MappExampleComponent
    private fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(
                CoreInjectHelper.provideMappComponent(applicationContext),
                DaggerCorePlatformComponent.factory().create()
            )
    }

    abstract fun setup()
}