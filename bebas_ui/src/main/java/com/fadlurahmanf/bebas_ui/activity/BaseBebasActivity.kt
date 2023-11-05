package com.fadlurahmanf.bebas_ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

typealias BebasInflateActivity<VB> = (LayoutInflater) -> VB

abstract class BaseBebasActivity<VB : ViewBinding>(
    private val inflater: BebasInflateActivity<VB>
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
        binding = inflater.invoke(layoutInflater)
        setContentView(binding.root)
    }

    abstract fun initComponent()

    abstract fun injectActivity()

    abstract fun setup()
}