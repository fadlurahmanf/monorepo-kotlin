package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetDestinationBankAccountBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
import com.fadlurahmanf.bebas_ui.extension.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DestinationBankAccountBottomsheet :
    BaseTransactionBottomsheet<BottomsheetDestinationBankAccountBinding>(
        BottomsheetDestinationBankAccountBinding::inflate
    ), BebasEdittext.BebasEdittextTextWatcher {
    companion object {
        const val BANK_IMAGE = "BANK_IMAGE"
        const val BANK_NAME = "BANK_NAME"
        const val INITIAL_DESTINATION_ACCOUNT = "INITIAL_DESTINATION_ACCOUNT"
    }

    private var bankImage: String? = null
    private var bankName: String? = null
    private var destinationAccountNumber: String? = null

    private var handler = Handler(Looper.getMainLooper())
    override fun setup() {
        bankImage = arguments?.getString(BANK_IMAGE)
        bankName = arguments?.getString(BANK_NAME)
        destinationAccountNumber = arguments?.getString(INITIAL_DESTINATION_ACCOUNT)

        Glide.with(binding.itemBank.ivBankLogo).load(Uri.parse(bankImage ?: ""))
            .into(binding.itemBank.ivBankLogo)
        binding.itemBank.tvBankName.text = bankName ?: "-"
        binding.etDestinationAccount.text = destinationAccountNumber ?: ""

        handler.postDelayed({
                                binding.etDestinationAccount.requestFocus()
                            }, 500)

        handler.postDelayed({
                                binding.etDestinationAccount.showKeyboard()
                                (dialog as BottomSheetDialog).behavior.state =
                                    BottomSheetBehavior.STATE_EXPANDED
                            }, 1000)

        binding.etDestinationAccount.addTextChangedListener(this)


        binding.btnNext.setOnClickListener {

        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun isCancelable(): Boolean {
        return false
    }

    override fun afterTextChanged(s: Editable?) {
        if (s?.toString()?.isEmpty() == true) {
            binding.etDestinationAccount.setError("Nomor rekening dibutuhkan", fieldError = true)
        } else if ((s?.toString()?.length ?: 0) < 10) {
            binding.etDestinationAccount.setError(
                "Nomor rekening minimal 10 angka",
                fieldError = true
            )
        } else {
            binding.etDestinationAccount.removeError()
        }
    }
}