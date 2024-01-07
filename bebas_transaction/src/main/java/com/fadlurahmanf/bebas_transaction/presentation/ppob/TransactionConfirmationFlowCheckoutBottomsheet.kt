package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationCheckoutArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.TransactionConfirmationCheckoutFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransactionConfirmationCheckoutBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class TransactionConfirmationFlowCheckoutBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransactionConfirmationCheckoutBinding>(
        BottomsheetTransactionConfirmationCheckoutBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    private lateinit var argument: TransactionConfirmationCheckoutArgument
    private lateinit var flow: TransactionConfirmationCheckoutFlow

    private lateinit var feeDetailAdapter: TransactionDetailAdapter
    private lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<TransactionConfirmationArgument.FeeDetail.Detail> =
        arrayListOf()
    private val details: ArrayList<TransactionConfirmationArgument.Detail> = arrayListOf()

    @Inject
    lateinit var viewModel: TransactionConfirmationFlowCheckoutViewModel

    private lateinit var bottomsheetDialog: BottomSheetDialog

    override fun injectBottomsheet() {
        component.inject(this)
    }

    override fun setup() {
        val stringFlow = arguments?.getString(FLOW) ?: return
        flow = enumValueOf<TransactionConfirmationCheckoutFlow>(stringFlow)

        val arg: TransactionConfirmationCheckoutArgument?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg =
                arguments?.getParcelable(
                    ADDITIONAL_ARG,
                    TransactionConfirmationCheckoutArgument::class.java
                )
        } else {
            arg = arguments?.getParcelable<TransactionConfirmationCheckoutArgument>(ADDITIONAL_ARG)
        }

        if (arg == null) {
            return
        }

        bottomsheetDialog = (dialog as BottomSheetDialog)
        Handler(Looper.getMainLooper()).postDelayed({
                                                        bottomsheetDialog.behavior.state =
                                                            BottomSheetBehavior.STATE_EXPANDED
                                                    }, 1000)

        argument = arg

        setupIdentity()


        binding.btnNext.setOnClickListener {
            when (flow) {
                else -> {

                }
            }
        }

        binding.lItemPaymentSource.setOnClickListener {

        }
    }

    private fun setupIdentity() {
        when (flow) {
            TransactionConfirmationCheckoutFlow.PLN_PREPAID -> {
                binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
                binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
                Glide.with(binding.itemDestinationAccount.ivLogo).load(R.drawable.il_telkom_logo)
                    .into(binding.itemDestinationAccount.ivLogo)

                binding.itemDestinationAccount.tvLabel.text = argument.destinationLabel
                binding.itemDestinationAccount.tvSubLabel.text = argument.destinationSubLabel
            }
        }
    }

    interface Callback {
        fun onButtonTransactionConfirmationClicked(result: TransactionConfirmationResult)
        fun onChangePaymentSource(selectedPaymentSource: PaymentSourceModel)
    }
}