package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.os.Build
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.transfer.TransferConfirmationModel
import com.fadlurahmanf.bebas_transaction.data.flow.TransferConfirmationFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetTransferConfirmationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet

class TransferConfirmationBottomsheet :
    BaseTransactionBottomsheet<BottomsheetTransferConfirmationBinding>(
        BottomsheetTransferConfirmationBinding::inflate
    ) {

    companion object {
        const val ADDITIONAL_ARG = "ADDITIONAL_ARG"
        const val FLOW = "FLOW"
    }

    private lateinit var additinalArg: TransferConfirmationModel
    private var flow: TransferConfirmationFlow? = null

    override fun setup() {
        val stringFlow = arguments?.getString(FLOW) ?: return

        flow = enumValueOf<TransferConfirmationFlow>(stringFlow)

        val arg: TransferConfirmationModel?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arg =
                arguments?.getParcelable(ADDITIONAL_ARG, TransferConfirmationModel::class.java)
        } else {
            arg = arguments?.getParcelable<TransferConfirmationModel>(ADDITIONAL_ARG)
        }

        if (arg == null) {
            return
        }

        additinalArg = arg

        when (flow) {
            TransferConfirmationFlow.TRANSFER_BETWEEN_BANK_MAS -> {
                binding.itemDestinationAccount.ivLogo.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.il_logo_bebas_payment_source
                    )
                )
            }

            else -> {

            }
        }

        binding.itemDestinationAccount.tvAccountName.text = additinalArg.realAccountName
        binding.itemDestinationAccount.tvSavingTypeAndAccountNumber.text =
            "MAS Saving • ${additinalArg.destinationAccountNumber}"
        binding.tvNominal.text =
            additinalArg.nominal.toRupiahFormat(useSymbol = true, useDecimal = false)

    }
}