package com.fadlurahmanf.bebas_ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

typealias BebasInflateFragment<VB> = (LayoutInflater, ViewGroup?, Boolean) -> VB

abstract class BaseBebasFragment<VB : ViewBinding>(
    private val fragmentInflater: BebasInflateFragment<VB>
) : Fragment() {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = fragmentInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBebasCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBebasViewCreated(view, savedInstanceState)
    }

    abstract fun onBebasCreate(savedInstanceState: Bundle?)

    abstract fun onBebasViewCreated(view: View, savedInstanceState: Bundle?)
}