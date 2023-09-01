package com.fadlurahmanf.mapp_config.presentation

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.core_config.presentation.BaseActivity
import com.fadlurahmanf.core_config.presentation.InflateActivity

typealias MappInflateActivity<VB> = (LayoutInflater) -> VB

abstract class BaseMappActivity<VB : ViewBinding>(
    private val inflater: InflateActivity<VB>
) : BaseActivity<VB>(inflater) {

}

