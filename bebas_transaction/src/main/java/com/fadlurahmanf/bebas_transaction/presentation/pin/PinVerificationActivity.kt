package com.fadlurahmanf.bebas_transaction.presentation.pin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.flow.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPinVerificationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.invoice.InvoiceTransactionActivity
import com.fadlurahmanf.bebas_ui.pin.BebasPinKeyboard
import javax.inject.Inject

class PinVerificationActivity :
    BaseTransactionActivity<ActivityPinVerificationBinding>(ActivityPinVerificationBinding::inflate),
    BebasPinKeyboard.Callback {

    companion object {
        const val PIN_VERIFICATION_ARGUMENT = "PIN_VERIFICATION_ARGUMENT"
    }

    lateinit var argument: PinVerificationArgument

    @Inject
    lateinit var viewModel: PinVerificationViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                PIN_VERIFICATION_ARGUMENT,
                PinVerificationArgument::class.java
            )
        } else {
            intent.getParcelableExtra(PIN_VERIFICATION_ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("ARG_MISSING"))
            return
        }

        argument = p0Arg

        binding.ivPinKeyboard.setCallback(this)

        viewModel.totalPinAttemptState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    if ((it.data.attemptCount ?: 0) > 0) {
                        binding.ivPinKeyboard
                    }
                }

                else -> {

                }
            }
        }

        viewModel.fundTransferState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    goToInvoice(it.data)
                }

                else -> {

                }
            }
        }

        viewModel.getTotalPinAttempt()
    }

    private fun goToInvoice(data: FundTransferResponse) {
        val intent = Intent(this, InvoiceTransactionActivity::class.java)
        intent.putExtra(
            InvoiceTransactionActivity.FLOW,
            InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS.name
        )
        intent.putExtra(
            InvoiceTransactionActivity.ARGUMENT, InvoiceTransactionArgument(
                statusTransaction = "SUCCESS",
                transactionId = data.transactionId ?: "-",
                isFavorite = false,
                isFavoriteEnabled = false,
                transactionDate = data.transactionDateTime ?: "-"
            )
        )
        startActivity(intent)
    }

    private var pin: String = ""

    override fun onPinClicked(pin: String) {
        if (this.pin.length < 6) {
            this.pin += pin
            binding.bebasPinBox.pin = this.pin
        }

        if (this.pin.length == 6) {
            viewModel.fundTransferBankMas(this.pin, argument.fundTransferBankMAS!!)
        }
    }

    override fun onForgotPinClicked() {}

    override fun onDeletePinClicked() {
        if (pin.isNotEmpty()) {
            pin = pin.substring(0, pin.length - 1)
            binding.bebasPinBox.pin = pin
        }
    }

}