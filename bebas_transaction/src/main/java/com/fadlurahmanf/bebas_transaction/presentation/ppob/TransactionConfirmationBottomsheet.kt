package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransactionConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.TransactionConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransactionConfirmationBinding
import com.fadlurahmanf.bebas_transaction.external.TransactionConfirmationCallback
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.TransactionDetailAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class TransactionConfirmationBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransactionConfirmationBinding>(
        BottomsheetTransactionConfirmationBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private var callback: TransactionConfirmationCallback? = null

    fun setCallback(callback: TransactionConfirmationCallback) {
        this.callback = callback
    }

    private lateinit var argument: TransactionConfirmationArgument
    private lateinit var flow: TransactionConfirmationFlow

    private lateinit var feeDetailAdapter: TransactionDetailAdapter
    private lateinit var detailAdapter: TransactionDetailAdapter
    private val feeDetails: ArrayList<TransactionConfirmationArgument.FeeDetail.Detail> =
        arrayListOf()
    private val details: ArrayList<TransactionConfirmationArgument.Detail> = arrayListOf()

    @Inject
    lateinit var viewModel: PaymentDetailViewModel

    private lateinit var bottomsheetDialog: BottomSheetDialog

    override fun injectBottomsheet() {
        component.inject(this)
    }

    override fun setup() {
        val stringFlow = arguments?.getString(FLOW) ?: return
        flow = enumValueOf<TransactionConfirmationFlow>(stringFlow)

        val arg: TransactionConfirmationArgument?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg =
                arguments?.getParcelable(
                    ADDITIONAL_ARG,
                    TransactionConfirmationArgument::class.java
                )
        } else {
            arg = arguments?.getParcelable<TransactionConfirmationArgument>(ADDITIONAL_ARG)
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
        setupFeeDetails()
        setupDetails()

        viewModel.selectedPaymentSourceState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.VISIBLE
                    binding.lItemPaymentSource.visibility = View.GONE
                }

                is NetworkState.LOADING -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.VISIBLE
                    binding.lItemPaymentSource.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    binding.lItemPaymentSourceShimmer.visibility = View.GONE
                    binding.lItemPaymentSource.visibility = View.VISIBLE

                    binding.itemPaymentSource.tvAccountName.text = it.data.accountName ?: "-"
                    binding.itemPaymentSource.tvSavingTypeAndAccountNumber.text =
                        "MAS Saving • ${it.data.bankAccountResponse?.accountNumber ?: "-"}"
                    binding.itemPaymentSource.tvAccountBalance.text =
                        it.data.balance.toRupiahFormat(
                            useSymbol = true,
                            useDecimal = true
                        )

                    if (argument.feeDetail.total > it.data.balance) {
                        binding.btnNext.setActive(false)
                        binding.tvBalanceNotEnough.visibility = View.VISIBLE
                    } else {
                        binding.btnNext.setActive(true)
                        binding.tvBalanceNotEnough.visibility = View.GONE
                    }
                }

                else -> {

                }
            }
        }

        if (argument.defaultPaymentSource != null) {
            viewModel.selectPaymentSource(argument.defaultPaymentSource!!)
        } else {
            viewModel.getPaymentSources()
        }

        binding.btnNext.setOnClickListener {
            when (flow) {
                TransactionConfirmationFlow.TELKOM_INDIHOME -> {
                    callback?.onButtonTransactionConfirmationClicked(
                        result = TransactionConfirmationResult(
                            selectedAccountNumber = viewModel.selectedPaymentSource?.accountNumber
                                ?: "-",
                            selectedAccountName = viewModel.selectedPaymentSource?.accountName
                                ?: "-"
                        )
                    )
                }

                TransactionConfirmationFlow.TV_CABLE -> {
//                    callback?.onButtonTransactionConfirmationClicked(
//                        result = TransactionConfirmationResult(
//                            selectedAccountNumber = viewModel.selectedPaymentSource?.accountNumber
//                                ?: "-",
//                            selectedAccountName = viewModel.selectedPaymentSource?.accountName
//                                ?: "-"
//                        )
//                    )
                }

                TransactionConfirmationFlow.PULSA -> {
                    callback?.onButtonTransactionConfirmationClicked(
                        result = TransactionConfirmationResult(
                            selectedAccountNumber = viewModel.selectedPaymentSource?.accountNumber
                                ?: "-",
                            selectedAccountName = viewModel.selectedPaymentSource?.accountName
                                ?: "-"
                        )
                    )
                }
            }
        }

        binding.lItemPaymentSource.setOnClickListener {
            Log.d("BebasLogger", "ITEM PAYMENT SOURCE: ${viewModel.selectedPaymentSource}")
            Log.d("BebasLogger", "CALLBACK: ${callback != null}")
            if (viewModel.selectedPaymentSource != null) {
                callback?.onChangePaymentSource(viewModel.selectedPaymentSource!!)
            }
        }
    }

    private fun setupIdentity() {
        when (flow) {
            TransactionConfirmationFlow.TELKOM_INDIHOME -> {
                binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
                binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
                Glide.with(binding.itemDestinationAccount.ivLogo).load(R.drawable.il_telkom_logo)
                    .into(binding.itemDestinationAccount.ivLogo)

                binding.itemDestinationAccount.tvLabel.text = argument.destinationLabel
                binding.itemDestinationAccount.tvSubLabel.text = argument.destinationSubLabel
            }

            TransactionConfirmationFlow.TV_CABLE -> {
                binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
                binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
//                Glide.with(binding.itemDestinationAccount.ivLogo).load(R.drawable.il_telkom_logo)
//                    .into(binding.itemDestinationAccount.ivLogo)

                binding.itemDestinationAccount.tvLabel.text = argument.destinationLabel
                binding.itemDestinationAccount.tvSubLabel.text = argument.destinationSubLabel
            }

            TransactionConfirmationFlow.PULSA -> {
                binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
                binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
                Glide.with(binding.itemDestinationAccount.ivLogo)
                    .load(Uri.parse(argument.imageLogoUrl ?: ""))
                    .into(binding.itemDestinationAccount.ivLogo)

                binding.itemDestinationAccount.tvLabel.text = argument.destinationLabel
                binding.itemDestinationAccount.tvSubLabel.text = argument.destinationSubLabel
            }
        }
    }

    private fun setupFeeDetails() {
        feeDetails.clear()
        feeDetails.addAll(argument.feeDetail.details)
        feeDetailAdapter = TransactionDetailAdapter()
        feeDetailAdapter.setList(feeDetails.map { nestedDetail ->
            TransactionDetailModel(
                label = nestedDetail.label,
                value = nestedDetail.value.toRupiahFormat(
                    useSymbol = true,
                    useDecimal = false,
                    freeIfZero = true
                ),
            )
        })
        binding.rvFeeDetail.adapter = feeDetailAdapter
        binding.tvTotal.text =
            argument.feeDetail.total.toRupiahFormat(useSymbol = true, useDecimal = false)
    }

    private fun setupDetails() {
        details.clear()
        details.addAll(argument.details)
        detailAdapter = TransactionDetailAdapter()
        detailAdapter.setList(details.map { nestedDetail ->
            TransactionDetailModel(
                label = nestedDetail.label,
                value = nestedDetail.value,
                valueStyle = nestedDetail.valueStyle
            )
        })
        binding.rvDetails.adapter = detailAdapter

        if (details.isNotEmpty()) {
            binding.llDetail.visibility = View.VISIBLE
        } else {
            binding.llDetail.visibility = View.GONE
        }
    }
}