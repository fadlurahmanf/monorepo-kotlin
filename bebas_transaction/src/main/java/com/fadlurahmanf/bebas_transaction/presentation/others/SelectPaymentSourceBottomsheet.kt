package com.fadlurahmanf.bebas_transaction.presentation.others

import android.os.Build
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.data.dto.argument.SelectPaymentSourceArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.flow.SelectPaymentSourceFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetSelectPaymentSourceBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.PaymentSourceAdapter
import javax.inject.Inject

class SelectPaymentSourceBottomsheet :
    BaseTransactionBottomsheet<BottomsheetSelectPaymentSourceBinding>(
        BottomsheetSelectPaymentSourceBinding::inflate
    ), PaymentSourceAdapter.Callback {

    @Inject
    lateinit var viewModel: SelectPaymentSourceViewModel

    companion object {
        const val ARGUMENT = "ARGUMENT"
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    private lateinit var paymentSourceArgument: SelectPaymentSourceArgument
    private lateinit var flow: SelectPaymentSourceFlow

    private lateinit var adapter: PaymentSourceAdapter
    private val paymentSources: ArrayList<PaymentSourceModel> = arrayListOf()
    override fun injectBottomsheet() {
        component.inject(this)
    }

    override fun setup() {
        val p0Arg: SelectPaymentSourceArgument?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p0Arg = arguments?.getParcelable(ARGUMENT, SelectPaymentSourceArgument::class.java)
        } else {
            p0Arg = arguments?.getParcelable(ARGUMENT)
        }

        if (p0Arg == null) {
            return
        }

        paymentSourceArgument = p0Arg
        flow = paymentSourceArgument.flow

        adapter = PaymentSourceAdapter()
        adapter.setCallback(this)
        adapter.setList(paymentSources)
        binding.rvPaymentSource.adapter = adapter

        viewModel.paymentSourcesState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.LOADING -> {

                }

                is NetworkState.SUCCESS -> {
                    paymentSources.clear()
                    paymentSources.addAll(it.data)
                    adapter.setList(paymentSources)
                }

                else -> {

                }
            }
        }

        viewModel.getPaymentSources()
    }

    override fun onSelectPaymentSource(paymentSource: PaymentSourceModel) {
        callback?.onSelectPaymentSource(paymentSource)
    }

    interface Callback {
        fun onSelectPaymentSource(paymentSource: PaymentSourceModel)
    }
}