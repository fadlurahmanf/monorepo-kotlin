package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityTransferDetailBinding
import com.fadlurahmanf.bebas_transaction.external.BebasKeyboardTransaction
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity

class TransferDetailActivity :
    BaseTransactionActivity<ActivityTransferDetailBinding>(ActivityTransferDetailBinding::inflate) {

    companion object {
        const val IS_FAVORITE = "IS_FAVORITE"
        const val DESTINATION_ACCOUNT_NAME = "DESTINATION_ACCOUNT_NAME"
        const val SUB_LABEL = "SUB_LABEL"
    }

    var isFavorite: Boolean = false
    var destinationAccountName: String = "-"
    var subLabel: String = "-"

    override fun injectActivity() {
        component.inject(this)
    }

    private var isKeyboardVisible = false
    private var nominal: Long? = null

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        isFavorite = intent.getBooleanExtra(IS_FAVORITE, false)
        destinationAccountName = intent.getStringExtra(DESTINATION_ACCOUNT_NAME) ?: "-"
        subLabel = intent.getStringExtra(SUB_LABEL) ?: "-"

        binding.layoutInputNominal.tvDestinationAccountName.text = destinationAccountName
        binding.layoutInputNominal.tvSubLabel.text = subLabel
        if (isFavorite) {
            var initial = "-"
            if (destinationAccountName.contains(" ")) {
                val first = destinationAccountName.split(" ").first().take(1)
                val second = destinationAccountName.split(" ")[1].take(1)
                binding.layoutInputNominal.initialAvatar.text = "$first$second"
            } else {
                binding.layoutInputNominal.initialAvatar.text = destinationAccountName.take(1)
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

        binding.btnNext.setOnClickListener {

        }
    }

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

}