package com.fadlurahmanf.bebas_transaction.presentation.others

import android.app.Dialog
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.flow.InputDestinationAccountFlow
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetInputDestinationAccountBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
import com.fadlurahmanf.bebas_ui.extension.dismissKeyboard
import com.fadlurahmanf.bebas_ui.extension.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class InputDestinationAccountBottomsheet :
    BaseTransactionBottomsheet<BottomsheetInputDestinationAccountBinding>(
        BottomsheetInputDestinationAccountBinding::inflate
    ), BebasEdittext.BebasEdittextTextWatcher {
    companion object {
        const val FLOW = "FLOW"

        const val IMAGE_NEW_RECEIVER_LOGO = "IMAGE_NEW_RECEIVER_LOGO"
        const val LABEL_NEW_RECEIVER = "LABEL_NEW_RECEIVER"
        const val INITIAL_DESTINATION_ACCOUNT = "INITIAL_DESTINATION_ACCOUNT"
    }

    private lateinit var flow: InputDestinationAccountFlow

    private var imageUrl: String? = null
    private var label: String? = null
    private var destinationAccountNumber: String? = null

    private var handler = Handler(Looper.getMainLooper())

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    private lateinit var bottomsheetDialog: BottomSheetDialog
    override fun injectBottomsheet() {}

    override fun setup() {
        val stringFlow = arguments?.getString(FLOW) ?: return
        flow = enumValueOf<InputDestinationAccountFlow>(stringFlow)

        imageUrl = arguments?.getString(IMAGE_NEW_RECEIVER_LOGO)
        label = arguments?.getString(LABEL_NEW_RECEIVER)
        destinationAccountNumber = arguments?.getString(INITIAL_DESTINATION_ACCOUNT)

        when (flow) {
            InputDestinationAccountFlow.TRANSFER -> {
                binding.tvTitleBottomsheet.text = "Masukkan Rekening Tujuan"
                binding.etDestinationAccount.setLabel("Nomor Rekening")
            }

            InputDestinationAccountFlow.PLN_PREPAID -> {
                binding.tvTitleBottomsheet.text = "Masukkan ID Pel/No. Meter"
                binding.etDestinationAccount.setLabel("ID Pelanggan/No. Meter")
            }

            InputDestinationAccountFlow.PLN_POSTPAID -> {
                binding.tvTitleBottomsheet.text = "Masukkan ID Pel/No. Meter"
                binding.etDestinationAccount.setLabel("ID Pelanggan/No. Meter")
            }
        }

        when (flow) {
            InputDestinationAccountFlow.PLN_PREPAID -> {
                binding.tvNewReceiverLabel.text = "Token Listrik"
                Glide.with(binding.ivNewReceiverLogo)
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.il_logo_pln))
                    .into(binding.ivNewReceiverLogo)
            }

            InputDestinationAccountFlow.PLN_POSTPAID -> {
                binding.tvNewReceiverLabel.text = "Tagihan Listrik"
                Glide.with(binding.ivNewReceiverLogo)
                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.il_logo_pln))
                    .into(binding.ivNewReceiverLogo)
            }

            else -> {
                binding.tvNewReceiverLabel.text = label ?: "-"
                binding.etDestinationAccount.text = destinationAccountNumber ?: ""
                Glide.with(binding.ivNewReceiverLogo).load(Uri.parse(imageUrl ?: ""))
                    .into(binding.ivNewReceiverLogo)
            }
        }

        bottomsheetDialog = (dialog as BottomSheetDialog)


        handler.postDelayed({
                                binding.etDestinationAccount.requestFocus()
                            }, 500)

        handler.postDelayed({
                                binding.etDestinationAccount.showKeyboard()
                                bottomsheetDialog.behavior.state =
                                    BottomSheetBehavior.STATE_EXPANDED
                            }, 1000)

        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        bottomsheetDialog.behavior.isDraggable = false

        binding.etDestinationAccount.addTextChangedListener(this)

        binding.ivBack.setOnClickListener {
            dialog?.dismiss()
        }

        binding.btnNext.setOnClickListener {
            binding.etDestinationAccount.clearFocus()
            binding.etDestinationAccount.dismissKeyboard()
            verifyAccountNumberField()
            if (isCanTapNext) {
                callback?.onNextClicked(dialog, binding.etDestinationAccount.text)
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    private var isCanTapNext = false

    override fun afterTextChanged(s: Editable?) {
        verifyAccountNumberField()
    }

    private fun verifyAccountNumberField() {
        if (binding.etDestinationAccount.text.isEmpty()) {
            isCanTapNext = false
            binding.etDestinationAccount.setError("Nomor rekening dibutuhkan", fieldError = true)
        } else if (binding.etDestinationAccount.text.length < 10) {
            isCanTapNext = false
            binding.etDestinationAccount.setError(
                "Nomor rekening minimal 10 angka",
                fieldError = true
            )
        } else {
            isCanTapNext = true
            binding.etDestinationAccount.removeError()
        }
    }

    interface Callback {
        fun onNextClicked(dialog: Dialog?, destinationAccount: String) {
            dialog?.dismiss()
        }
    }
}