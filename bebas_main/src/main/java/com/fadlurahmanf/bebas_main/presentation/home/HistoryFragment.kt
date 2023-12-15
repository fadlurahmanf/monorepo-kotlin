package com.fadlurahmanf.bebas_main.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.View
import com.fadlurahmanf.bebas_main.databinding.FragmentHistoryBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : BaseMainFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {
    private var param1: String? = null
    private var param2: String? = null
    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d("BebasLogger", "ON CREATE HISTORY")
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("BebasLogger", "ON VIEW CREATED HISTORY")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}