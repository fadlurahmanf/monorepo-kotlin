package com.fadlurahmanf.bebas_ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

typealias InflateDialog<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseDialog<VB : ViewBinding>(
    private val inflate: InflateDialog<VB>
) : DialogFragment() {
    private lateinit var _binding: VB
    val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    abstract fun setup()
}