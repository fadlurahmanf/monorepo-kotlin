package com.fadlurahmanf.bebas_main.presentation.home

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_main.databinding.FragmentHomeBinding
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeBebasFragment : BaseBebasFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private var param1: String? = null
    private var param2: String? = null

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {}

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeBebasFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}