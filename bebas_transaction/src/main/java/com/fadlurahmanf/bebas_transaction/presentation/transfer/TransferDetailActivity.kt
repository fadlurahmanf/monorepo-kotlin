package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityTransferDetailBinding
import com.fadlurahmanf.bebas_transaction.external.BebasKeyboardTransaction
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity

class TransferDetailActivity :
    BaseTransactionActivity<ActivityTransferDetailBinding>(ActivityTransferDetailBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    private var isVisible = false
    private var tvAmountIsAHint: Boolean = true

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
                                                        dismissKeyboardTransaction()
                                                    }, 500)

        binding.layoutInputNominal.llInputNominal.setOnClickListener {
            if (isVisible) {
                dismissKeyboardTransaction()
            } else {
                openKeyboardTransaction()
            }
        }

        binding.keyboard.setOnClickKeyboard(object : BebasKeyboardTransaction.CallbackKeyboard {
            override fun onDigitClicked(digit: String) {
                if (tvAmountIsAHint) {
                    tvAmountIsAHint = false
                    binding.layoutInputNominal.tvAmount.text = digit.toInt().toString()
                } else {
                    val isCurrentTvAmountEmptyOrZero =
                        binding.layoutInputNominal.tvAmount.text.isEmpty() || binding.layoutInputNominal.tvAmount.text.toString()
                            .toInt() == 0
                    if (isCurrentTvAmountEmptyOrZero && digit.toInt() == 0) {
                        return
                    } else if (isCurrentTvAmountEmptyOrZero && digit.toInt() > 0) {
                        binding.layoutInputNominal.tvAmount.text = digit
                    } else {
                        val currentText = binding.layoutInputNominal.tvAmount.text
                        binding.layoutInputNominal.tvAmount.text = "$currentText$digit"
                    }
                }
                updateStyleTvAmount()
            }

            override fun onClearClicked() {
                if (!tvAmountIsAHint) {
                    binding.layoutInputNominal.tvAmount.text = ""
                    updateStyleTvAmount()
                }
            }

            override fun onBackspaceClicked() {
                if (!tvAmountIsAHint) {
                    if (binding.layoutInputNominal.tvAmount.text.isNotEmpty()) {
                        val currentText = binding.layoutInputNominal.tvAmount.text.toString()
                        binding.layoutInputNominal.tvAmount.text =
                            currentText.substring(0, currentText.length - 1)
                        updateStyleTvAmount()
                    }
                }
            }

            override fun onFinish() {
                dismissKeyboardTransaction()
            }
        })
    }

    private fun openKeyboardTransaction() {
        isVisible = true
        binding.layoutInputNominal.llInputNominal.background = ContextCompat.getDrawable(
            this,
            R.drawable.background_total_amount_transfer_detail_active
        )
        binding.keyboard.animate().translationY(0f)
    }

    private fun dismissKeyboardTransaction() {
        isVisible = false
        binding.layoutInputNominal.llInputNominal.background = ContextCompat.getDrawable(
            this,
            R.drawable.background_total_amount_transfer_detail
        )
        binding.keyboard.animate().translationY(binding.keyboard.height.toFloat())
    }

    fun updateStyleTvAmount() {
        if (binding.layoutInputNominal.tvAmount.text.isEmpty()) {
            tvAmountIsAHint = true
            binding.layoutInputNominal.tvAmount.text = getString(R.string.input_nominal_transfer)
            binding.layoutInputNominal.tvAmount.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
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