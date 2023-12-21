package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.flow.TransferConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransferConfirmationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.adapter.TransferConfirmationDetailAdapter
import javax.inject.Inject

class TransferConfirmationBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransferConfirmationBinding>(
        BottomsheetTransferConfirmationBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private lateinit var additinalArg: TransferConfirmationArgument
    private var flow: TransferConfirmationFlow? = null

    private lateinit var adapter: TransferConfirmationDetailAdapter
    private val details: ArrayList<TransferConfirmationArgument.Detail> = arrayListOf()

    @Inject
    lateinit var viewModel: TransferDetailViewModel

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

        additinalArg = arg
        details.clear()
        details.addAll(additinalArg.details)

        adapter = TransferConfirmationDetailAdapter()
        adapter.setList(details)
        binding.rvDetails.adapter = adapter

        if (details.isNotEmpty()) {
            binding.llDetail.visibility = View.VISIBLE
        } else {
            binding.llDetail.visibility = View.GONE
        }


        viewModel.selectedBankAccount.observe(this) {
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

        if (additinalArg.imageLogoUrl != null) {
            Glide.with(binding.itemDestinationAccount.ivLogo)
                .load(Uri.parse(additinalArg.imageLogoUrl))
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
        }

        binding.itemDestinationAccount.tvAccountName.text = additinalArg.realAccountName
        binding.itemDestinationAccount.tvSavingTypeAndAccountNumber.text =
            "MAS Saving • ${additinalArg.destinationAccountNumber}"
        binding.tvNominal.text =
            additinalArg.nominal.toRupiahFormat(useSymbol = true, useDecimal = false)

        viewModel.getBankAccounts()

    }
}