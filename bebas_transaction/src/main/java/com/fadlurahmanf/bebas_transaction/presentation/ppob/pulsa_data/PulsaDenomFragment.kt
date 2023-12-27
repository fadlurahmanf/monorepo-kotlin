package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PulsaDataArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PulsaDenomModel
import com.fadlurahmanf.bebas_transaction.databinding.FragmentPulsaDenomBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionFragment
import javax.inject.Inject


private const val PULSA_DATA_ARGUMENT = "PULSA_DATA_ARGUMENT"

class PulsaDenomFragment :
    BaseTransactionFragment<FragmentPulsaDenomBinding>(FragmentPulsaDenomBinding::inflate) {

    @Inject
    lateinit var viewModel: PulsaDataViewModel

    lateinit var argument: PulsaDataArgument
    private lateinit var adapter: PulsaDenomAdapter
    private val denoms: ArrayList<PulsaDenomModel> = arrayListOf()

    override fun injectFragment() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PULSA_DATA_ARGUMENT, PulsaDataArgument::class.java)
        } else {
            arguments?.getParcelable(PULSA_DATA_ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("ARGUMENT_MISSING"))
            return
        }

        argument = p0Arg

    }

    override fun onBebasViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!::argument.isInitialized) {
            showForcedBackBottomsheet(BebasException.generalRC("ARGUMENT_MISSING"))
            return
        }

        val gm = GridLayoutManager(requireContext(), 2)
        adapter = PulsaDenomAdapter()
        adapter.setList(denoms)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = gm

        viewModel.pulsaDenomState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.LOADING -> {

                }

                is NetworkState.SUCCESS -> {
                    denoms.clear()
                    denoms.addAll(it.data)
                    adapter.setList(denoms)
                }

                else -> {

                }
            }
        }


        viewModel.getPulsaDenom(argument.providerName, argument.providerImage)
    }

    companion object {
        @JvmStatic
        fun newInstance(arg: PulsaDataArgument) =
            PulsaDenomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PULSA_DATA_ARGUMENT, arg)
                }
            }
    }
}