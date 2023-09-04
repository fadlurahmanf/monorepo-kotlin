package com.fadlurahmanf.core_config.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
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

    abstract fun initComponent()

    abstract fun injectActivity()

    abstract fun setup()

    open fun tes(){

    }
}