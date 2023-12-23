package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.PinVerificationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferConfirmationArgument
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferDetailArgument
import com.fadlurahmanf.bebas_transaction.data.flow.TransferConfirmationFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransferDetailFlow
import com.fadlurahmanf.bebas_transaction.data.state.TransferDetailState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityTransferDetailBinding
import com.fadlurahmanf.bebas_transaction.external.BebasKeyboardTransaction
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.pin.PinVerificationActivity
import javax.inject.Inject

class TransferDetailActivity :
    BaseTransactionActivity<ActivityTransferDetailBinding>(ActivityTransferDetailBinding::inflate) {

    companion object {
        const val FLOW = "FLOW"
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var flow: TransferDetailFlow
    lateinit var argument: TransferDetailArgument

    @Inject
    lateinit var viewModel: TransferDetailViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private var isKeyboardVisible = false
    private var nominal: Long? = null

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val stringFlow = intent.getStringExtra(FLOW)

        if (stringFlow == null) {
            showForcedBackBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, TransferDetailArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedBackBottomsheet(BebasException.generalRC("ARG_MISSING"))
            return
        }

        flow = enumValueOf<TransferDetailFlow>(stringFlow)
        argument = p0Arg

        binding.layoutInputNominal.tvDestinationAccountName.text = argument.accountName
        binding.layoutInputNominal.tvSubLabel.text =
            "${argument.bankName} • ${argument.accountNumber}"

        if (argument.bankImageUrl != null && !argument.isFavorite) {
            binding.layoutInputNominal.initialAvatar.visibility = View.GONE
            binding.layoutInputNominal.ivBankLogo.visibility = View.VISIBLE
            Glide.with(binding.layoutInputNominal.ivBankLogo)
                .load(Uri.parse(argument.bankImageUrl!!))
                .error(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.il_bebas_grey_transaction
                    )
                )
                .placeholder(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.il_bebas_grey_transaction
                    )
                )
                .into(binding.layoutInputNominal.ivBankLogo)
        } else if (argument.isFavorite) {
            binding.layoutInputNominal.initialAvatar.visibility = View.VISIBLE
            binding.layoutInputNominal.ivBankLogo.visibility = View.GONE
            if (argument.accountName.contains(" ")) {
                val first = argument.accountName.split(" ").first().take(1)
                val second = argument.accountName.split(" ")[1].take(1)
                binding.layoutInputNominal.initialAvatar.text = "$first$second"
            } else {
                binding.layoutInputNominal.initialAvatar.text = argument.accountName.take(1)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
                                                        dismissKeyboardTransaction()
                                                    }, 500)

        binding.layoutInputNominal.llInputNominal.setOnClickListener {
            if (isKeyboardVisible) {
                dismissKeyboardTransaction()
            } else {
                openKeyboardTransaction()
            }
        }

        binding.keyboard.setOnClickKeyboard(object : BebasKeyboardTransaction.CallbackKeyboard {
            override fun onDigitClicked(digit: String) {
                if (nominal == null) {
                    nominal = digit.toLong()
                    binding.layoutInputNominal.tvAmount.text =
                        (nominal ?: 0).toDouble().toRupiahFormat(useDecimal = false)
                } else {
                    val isCurrentTvAmountZero = nominal?.toInt() == 0
                    if (isCurrentTvAmountZero && digit.toInt() == 0) {
                        nominal = 0
                        binding.layoutInputNominal.tvAmount.text = "0"
                        return
                    } else if (isCurrentTvAmountZero && digit.toInt() > 0) {
                        nominal = digit.toLong()
                        binding.layoutInputNominal.tvAmount.text = digit
                    } else {
                        nominal = "$nominal${digit}".toLong()
                        binding.layoutInputNominal.tvAmount.text =
                            nominal?.toDouble()?.toRupiahFormat(
                                useDecimal = false
                            )
                    }
                }
                updateStyleTvAmount()
            }

            override fun onClearClicked() {
                if (nominal != null) {
                    nominal = null
                    updateStyleTvAmount()
                }
            }

            override fun onBackspaceClicked() {
                if (nominal != null) {
                    if (binding.layoutInputNominal.tvAmount.text.isNotEmpty()) {
                        val text = "$nominal".substring(0, nominal.toString().length - 1)
                        if (text.isEmpty()) {
                            nominal = null
                        } else {
                            nominal = text.toLong()
                        }
                        binding.layoutInputNominal.tvAmount.text =
                            nominal?.toDouble()?.toRupiahFormat(
                                useDecimal = false
                            )

                        updateStyleTvAmount()
                    }
                }
            }

            override fun onFinish() {
                dismissKeyboardTransaction()
            }
        })

        viewModel.transferState.observe(this) {
            when (it) {
                TransferDetailState.IDLE -> {
                    binding.tvErrorNominal.visibility = View.GONE
                }

                is TransferDetailState.MinimumNominalTransferFailed -> {
                    binding.tvErrorNominal.text = getString(
                        R.string.minimum_transfer_nominal_is, it.minimum.toDouble().toRupiahFormat(
                            useSymbol = false,
                            useDecimal = false
                        )
                    )
                    binding.tvErrorNominal.visibility = View.VISIBLE
                    openKeyboardTransaction()
                }

                is TransferDetailState.SUCCESS -> {
                    binding.tvErrorNominal.visibility = View.GONE
                    showConfirmationBottomsheet()
                }
            }
        }

        binding.btnNext.setOnClickListener {
            dismissKeyboardTransaction()
            handler.postDelayed({
                                    viewModel.verify(nominal ?: 0L)
                                }, 250)
        }

        val intent = Intent(this, PinVerificationActivity::class.java)
        intent.apply {
            putExtra(
                PinVerificationActivity.PIN_VERIFICATION_ARGUMENT, PinVerificationArgument(
                    fundTransferBankMAS = FundTransferBankMASRequest(
                        accountNumber = "1001934356",
                        destinationAccountName = argument.realAccountName,
                        destinationAccountNumber = argument.accountNumber,
                        amountTransaction = 20000,
                        description = "TES AJA",
                        ip = "0.0.0.0",
                        latitude = 0.0,
                        longitude = 0.0
                    )
                )
            )
        }
        startActivity(intent)
    }

    private var handler = Handler(Looper.getMainLooper())

    private fun openKeyboardTransaction() {
        isKeyboardVisible = true
        binding.layoutInputNominal.llInputNominal.background = ContextCompat.getDrawable(
            this,
            R.drawable.background_total_amount_transfer_detail_active
        )
        binding.keyboard.animate().translationY(0f)
    }

    private fun dismissKeyboardTransaction() {
        isKeyboardVisible = false
        binding.layoutInputNominal.llInputNominal.background = ContextCompat.getDrawable(
            this,
            R.drawable.background_total_amount_transfer_detail
        )
        binding.keyboard.animate().translationY(binding.keyboard.height.toFloat())
    }

    fun updateStyleTvAmount() {
        if (nominal == null) {
            binding.layoutInputNominal.tvAmount.text = getString(R.string.input_nominal_transfer)
            binding.layoutInputNominal.tvAmount.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        R.color.grey
                    )
                )
            )
        } else {
            binding.layoutInputNominal.tvAmount.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
            )
        }
    }

    private var bottomsheetTransferConfirmation: TransferConfirmationBottomsheet? = null
    private fun showConfirmationBottomsheet() {
        val details = arrayListOf<TransferConfirmationArgument.Detail>()
        details.add(
            TransferConfirmationArgument.Detail(
                label = "Catatan",
                value = binding.etNotes.text,
            )
        )
        details.add(
            TransferConfirmationArgument.Detail(
                label = "Biaya",
                value = "Pindahbuku:Gratis",
            )
        )
        details.add(
            TransferConfirmationArgument.Detail(
                label = "Total",
                value = nominal?.toDouble()?.toRupiahFormat(useSymbol = true) ?: "-",
                valueStyle = R.style.Font_DetailValueBold
            )
        )
        bottomsheetTransferConfirmation = TransferConfirmationBottomsheet()
        bottomsheetTransferConfirmation?.arguments = Bundle().apply {
            putString(
                TransferConfirmationBottomsheet.FLOW,
                TransferConfirmationFlow.TRANSFER_BETWEEN_BANK_MAS.name
            )
//            putParcelable(
//                TransferConfirmationBottomsheet.ADDITIONAL_ARG, TransferConfirmationArgument(
//                    realAccountName = destinationAccountName,
//                    destinationAccountNumber = destinationAccountNumber,
//                    imageLogoUrl = bankImageUrl,
//                    bankNickName = bankNickname,
//                    nominal = (nominal ?: -1L).toDouble(),
//                    details = details
//                )
//            )
        }

        bottomsheetTransferConfirmation?.show(
            supportFragmentManager,
            TransferConfirmationBottomsheet::class.java.simpleName
        )
    }

}