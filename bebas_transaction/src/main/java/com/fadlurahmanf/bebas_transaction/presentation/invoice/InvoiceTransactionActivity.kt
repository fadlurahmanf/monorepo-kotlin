package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.formatInvoiceTransaction
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_shared.extension.utcToLocal
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.flow.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity

class InvoiceTransactionActivity :
    BaseTransactionActivity<ActivityInvoiceTransactionBinding>(ActivityInvoiceTransactionBinding::inflate) {
    companion object {
        const val FLOW = "FLOW"
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var flow: InvoiceTransactionFlow
    lateinit var argument: InvoiceTransactionArgument

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Flow = intent.getStringExtra(FLOW)

        if (p0Flow == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(p0Flow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, InvoiceTransactionArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_ARG"))
            return
        }

        argument = p0Arg

        binding.tvTransactionId.text = "ID Transaksi: ${argument.transactionId}"
        binding.tvTransactionDate.text =
            argument.transactionDate.utcToLocal()?.formatInvoiceTransaction() ?: "-"

        setupTotalTransaction()

        Handler(Looper.getMainLooper()).postDelayed({
                                                        binding.llStatus.animate()
                                                            .translationY(binding.llStatus.height.toFloat())
                                                    }, 3000)
    }

    private fun setupTotalTransaction() {
        when (flow) {
            InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS -> {

            }

            InvoiceTransactionFlow.PULSA_PREPAID -> {

            }

            InvoiceTransactionFlow.TELKOM_INDIHOME -> {
                Glide.with(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                    .load(R.drawable.il_telkom_logo)
                    .into(binding.layoutInvoiceTotalTransaction.ivDestinationLogo)
                binding.layoutInvoiceTotalTransaction.tvDestinationAccountName.text =
                    argument.additionalTelkomIndihome?.destinationAccountName ?: "-"
                binding.layoutInvoiceTotalTransaction.tvSubLabel.text =
                    "Telkom • ${argument.additionalTelkomIndihome?.destinationAccountNumber ?: "-"}"
                binding.layoutInvoiceTotalTransaction.tvTotalPaymentValue.text =
                    argument.additionalTelkomIndihome?.totalTransaction?.toRupiahFormat(
                        useSymbol = true,
                        useDecimal = true
                    )
            }
        }
    }

}