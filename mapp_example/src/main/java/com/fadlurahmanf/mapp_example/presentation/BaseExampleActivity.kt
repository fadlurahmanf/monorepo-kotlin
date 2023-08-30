package com.fadlurahmanf.mapp_example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_example.MappExampleComponent
import com.fadlurahmanf.mapp_example.databinding.ActivityExampleBinding

typealias InflateActivity<T> = (LayoutInflater) -> T
abstract class BaseExampleActivity<VB : ViewBinding>(
    var inflate: InflateActivity<VB>
) : AppCompatActivity() {
    lateinit var binding: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        initComponent()
        injectActivity()
        super.onCreate(savedInstanceState)
        bindingView()
        setup()
    }

    open fun bindingView() {
        binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun injectActivity()

    lateinit var component: MappExampleComponent
    private fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(
                CoreInjectHelper.provideMappComponent(applicationContext),
                CoreInjectHelper.provideCorePlatformComponent(applicationContext)
            )
    }

    abstract fun setup()
}