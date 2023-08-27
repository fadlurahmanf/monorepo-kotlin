package com.fadlurahmanf.mapp_example.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_example.MappExampleComponent

abstract class BaseExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        initComponent()
        super.onCreate(savedInstanceState)
        setup()
    }

    lateinit var component: MappExampleComponent
    private fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(CoreInjectHelper.provideMappComponent(applicationContext))
    }

    abstract fun setup()
}