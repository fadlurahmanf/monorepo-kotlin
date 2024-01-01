package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransferConfirmationResult
import com.fadlurahmanf.bebas_transaction.data.flow.TransferConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransferConfirmationBinding
import com.fadlurahmanf.bebas_transaction.external.BebasTransactionHelper
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.adapter.TransferConfirmationDetailAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class TransferConfirmationBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransferConfirmationBinding>(
        BottomsheetTransferConfirmationBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    private lateinit var argument: TransferConfirmationArgument
    private var flow: TransferConfirmationFlow? = null

    private lateinit var adapter: TransferConfirmationDetailAdapter
    private val details: ArrayList<TransferConfirmationArgument.Detail> = arrayListOf()

    @Inject
    lateinit var viewModel: TransferDetailViewModel

    private lateinit var bottomsheetDialog: BottomSheetDialog

    override fun injectBottomsheet() {
        component.inject(this)
    }

    override fun setup() {
        val stringFlow = arguments?.getString(FLOW) ?: return

        flow = enumValueOf<TransferConfirmationFlow>(stringFlow)

        val arg: TransferConfirmationArgument?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg =
                arguments?.getParcelable(ADDITIONAL_ARG, TransferConfirmationArgument::class.java)
        } else {
            arg = arguments?.getParcelable<TransferConfirmationArgument>(ADDITIONAL_ARG)
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
        details.clear()
        details.addAll(argument.details)

        adapter = TransferConfirmationDetailAdapter()
        adapter.setList(details.map { nestedDetail ->
            TransactionDetailModel(
                label = nestedDetail.label,
                value = nestedDetail.value,
                valueStyle = nestedDetail.valueStyle
            )
        })
        binding.rvDetails.adapter = adapter

        if (details.isNotEmpty()) {
            binding.llDetail.visibility = View.VISIBLE
        } else {
            binding.llDetail.visibility = View.GONE
        }


        viewModel.selectedBankAccountState.observe(this) {
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
                        "MAS Saving • ${it.data.accountNumber ?: "-"}"
                    binding.itemPaymentSource.tvAccountBalance.text =
                        it.data.workingBalance?.toRupiahFormat(
                            useSymbol = true,
                            useDecimal = true
                        )
                }

                else -> {

                }
            }
        }

        if (argument.imageLogoUrl != null) {
            binding.itemDestinationAccount.ivLogo.visibility = View.VISIBLE
            binding.itemDestinationAccount.initialAvatar.visibility = View.GONE
            Glide.with(binding.itemDestinationAccount.ivLogo)
                .load(Uri.parse(argument.imageLogoUrl))
                .placeholder(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.il_bebas_grey_transaction
                    )
                )
                .error(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.il_bebas_grey_transaction
                    )
                )
                .into(binding.itemDestinationAccount.ivLogo)
        } else {
            binding.itemDestinationAccount.ivLogo.visibility = View.GONE
            binding.itemDestinationAccount.initialAvatar.visibility = View.VISIBLE
            binding.itemDestinationAccount.initialAvatar.text =
                BebasTransactionHelper.getInitial(argument.realAccountName)
        }

        binding.itemDestinationAccount.tvLabel.text = argument.realAccountName
        binding.itemDestinationAccount.tvSubLabel.text =
            "Bank ${argument.bankNickName} • ${argument.destinationAccountNumber}"
        binding.tvNominal.text =
            argument.nominal.toDouble().toRupiahFormat(useSymbol = true, useDecimal = false)

        viewModel.getBankAccounts()

        binding.btnNext.setOnClickListener {
            when (flow) {
                TransferConfirmationFlow.TRANSFER_BETWEEN_BANK_MAS -> {
                    callback?.onButtonConfirmationClicked(
                        result = TransferConfirmationResult(
                            selectedAccountNumber = viewModel.selectedBankAccount?.accountNumber
                                ?: "-",
                        )
                    )
                }

                else -> {

                }
            }
        }
    }

    interface Callback {
        fun onButtonConfirmationClicked(result: TransferConfirmationResult)
    }
}