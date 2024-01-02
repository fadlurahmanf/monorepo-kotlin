package com.fadlurahmanf.bebas_transaction.presentation.pin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationRequestModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationResultModel
import com.fadlurahmanf.bebas_transaction.data.flow.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.data.flow.PinVerificationFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPinVerificationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.invoice.InvoiceTransactionActivity
import com.fadlurahmanf.bebas_ui.pin.BebasPinKeyboard
import javax.inject.Inject

class PinVerificationActivity :
    BaseTransactionActivity<ActivityPinVerificationBinding>(ActivityPinVerificationBinding::inflate),
    BebasPinKeyboard.Callback {

    companion object {
        const val ARGUMENT = "ARGUMENT"
        const val FLOW = "FLOW"
    }

    private lateinit var argument: PinVerificationArgument
    private lateinit var flow: PinVerificationFlow

    @Inject
    lateinit var viewModel: PinVerificationViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val stringFlow = intent.getStringExtra(FLOW)

        if (stringFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(stringFlow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ARGUMENT,
                PinVerificationArgument::class.java
            )
        } else {
            intent.getParcelableExtra(ARGUMENT)
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

    private fun goToInvoice(result: PostingPinVerificationResultModel) {
        when (flow) {
            PinVerificationFlow.TRANSFER_BETWEEN_BANK_MAS -> {
                val intent = Intent(this, InvoiceTransactionActivity::class.java)
                intent.putExtra(
                    InvoiceTransactionActivity.FLOW,
                    InvoiceTransactionFlow.FUND_TRANSFER_BANK_MAS.name
                )
                intent.putExtra(
                    InvoiceTransactionActivity.ARGUMENT, InvoiceTransactionArgument(
                        statusTransaction = result.transactionStatus,
                        transactionId = result.tranferBankMas?.transactionId ?: "-",
                        isFavorite = false,
                        isFavoriteEnabled = false,
                        transactionDate = result.tranferBankMas?.transactionDateTime ?: "-"
                    )
                )
                startActivity(intent)
            }

            PinVerificationFlow.POSTING_PULSA_PREPAID -> {
                val intent = Intent(this, InvoiceTransactionActivity::class.java)
                intent.putExtra(
                    InvoiceTransactionActivity.FLOW,
                    InvoiceTransactionFlow.PULSA_PREPAID.name
                )
                intent.putExtra(
                    InvoiceTransactionActivity.ARGUMENT, InvoiceTransactionArgument(
                        transactionId = result.pulsaPrePaid?.transactionId ?: "-",
                        transactionDate = result.pulsaPrePaid?.transactionDateTime ?: "-",
                        isFavorite = false,
                        isFavoriteEnabled = true,
                        statusTransaction = result.transactionStatus,
                        additionalPulsaData = InvoiceTransactionArgument.PulsaData(
                            phoneNumber = "-"
                        )
                    )
                )
                startActivity(intent)
            }

            PinVerificationFlow.POSTING_TELKOM_INDIHOME -> {
                val intent = Intent(this, InvoiceTransactionActivity::class.java)
                intent.putExtra(
                    InvoiceTransactionActivity.FLOW,
                    InvoiceTransactionFlow.TELKOM_INDIHOME.name
                )
                intent.putExtra(
                    InvoiceTransactionActivity.ARGUMENT, InvoiceTransactionArgument(
                        statusTransaction = result.transactionStatus,
                        transactionId = result.telkomIndihome?.transactionId ?: "-",
                        isFavorite = false,
                        isFavoriteEnabled = false,
                        transactionDate = result.telkomIndihome?.transactionDateTime ?: "-",
                        additionalTelkomIndihome = InvoiceTransactionArgument.TelkomIndihome(
                            destinationAccountName = argument.additionalTelkomIndihome?.inquiryResponse?.customerName
                                ?: "-",
                            destinationAccountNumber = argument.additionalTelkomIndihome?.inquiryResponse?.customerNumber
                                ?: "-",
                            totalTransaction = (argument.additionalTelkomIndihome?.inquiryResponse?.amountTransaction
                                ?: -1.0) + (argument.additionalTelkomIndihome?.inquiryResponse?.transactionFee
                                ?: -1.0),
                            fromAccount = argument.additionalTelkomIndihome?.postingRequest?.fromAccount
                                ?: "-",
                            inquiryResponse = argument.additionalTelkomIndihome?.inquiryResponse!!,
                            postingResponse = result.telkomIndihome!!
                        )
                    )
                )
                startActivity(intent)
            }
        }
    }

    private var pin: String = ""

    override fun onPinClicked(pin: String) {
        if (this.pin.length < 6) {
            this.pin += pin
            binding.bebasPinBox.pin = this.pin
        }

        if (this.pin.length == 6) {
            when (flow) {
                PinVerificationFlow.TRANSFER_BETWEEN_BANK_MAS -> {
                    viewModel.postingPinVerification(
                        plainPin = this.pin,
                        request = PostingPinVerificationRequestModel.FundTranfeerBankMas(
                            fundTransferBankMASRequest = argument.fundTransferBankMAS!!
                        )
                    )
                }

                PinVerificationFlow.POSTING_PULSA_PREPAID -> {
                    viewModel.postingPinVerification(
                        this.pin, request = PostingPinVerificationRequestModel.PostingPulsaPrePaid(
                            postingPulsaPrePaidRequest = argument.pulsaPrePaidRequest!!
                        )
                    )
                }

                PinVerificationFlow.POSTING_TELKOM_INDIHOME -> {
                    viewModel.postingPinVerification(
                        this.pin,
                        request = PostingPinVerificationRequestModel.PostingTelkomIndihome(
                            postingTelkomIndihomeRequest = argument.additionalTelkomIndihome!!.postingRequest
                        )
                    )
                }
            }
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