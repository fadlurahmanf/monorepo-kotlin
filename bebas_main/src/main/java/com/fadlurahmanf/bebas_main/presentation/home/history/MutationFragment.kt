package com.fadlurahmanf.bebas_main.presentation.home.history

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_main.databinding.FragmentMutationBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MutationFragment : BaseMainFragment<FragmentMutationBinding>(FragmentMutationBinding::inflate) {
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
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MutationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}