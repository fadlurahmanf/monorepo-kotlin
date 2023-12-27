package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_transaction.databinding.FragmentPaketDataDenomBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PaketDataDenomFragment :
    BaseTransactionFragment<FragmentPaketDataDenomBinding>(FragmentPaketDataDenomBinding::inflate) {
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
            PaketDataDenomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}