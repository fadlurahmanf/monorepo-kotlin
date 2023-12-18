package com.fadlurahmanf.bebas_transaction.presentation.transfer

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.DialogFragment
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetDestinationBankAccountBinding
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet
import com.fadlurahmanf.bebas_ui.extension.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class DestinationBankAccountBottomsheet : BaseBottomsheet<BottomsheetDestinationBankAccountBinding>(
    BottomsheetDestinationBankAccountBinding::inflate
) {
    private var handler = Handler(Looper.getMainLooper())
    override fun setup() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialogTheme)
        handler.postDelayed({
                                binding.etDestinationAccount.requestFocus()
                            }, 500)

        handler.postDelayed({
                                binding.etDestinationAccount.showKeyboard()
                                (dialog as BottomSheetDialog).behavior.state =
                                    BottomSheetBehavior.STATE_EXPANDED
                            }, 1000)


    }
}