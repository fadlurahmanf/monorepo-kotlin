package com.fadlurahmanf.bebas_main.presentation.home.history

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.databinding.FragmentEStatementBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainFragment
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EStatementFragment :
    BaseMainFragment<FragmentEStatementBinding>(FragmentEStatementBinding::inflate) {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: EStatementViewModel

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel.estatementState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {}
                NetworkState.LOADING -> {

                }

                is NetworkState.SUCCESS -> {

                }

                else -> {

                }
            }
        }
    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getEStatements()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EStatementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}