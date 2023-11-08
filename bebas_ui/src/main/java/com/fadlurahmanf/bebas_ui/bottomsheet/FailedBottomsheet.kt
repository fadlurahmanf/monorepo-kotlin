package com.fadlurahmanf.bebas_ui.bottomsheet

import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetFailedBinding

class FailedBottomsheet : BaseBottomsheet<BottomsheetFailedBinding>(
    BottomsheetFailedBinding::inflate
) {

    private var callback: Callback? = null

    companion object {
        const val IS_DIALOG_CANCELABLE = "IS_DIALOG_CANCELABLE"
        const val DRAWABLE_RES = "DRAWABLE_RES"
        const val TITLE_TEXT = "TITLE_TEXT"
        const val MESSAGE_TEXT = "callback?.onButtonClicked()"
        const val BUTTON_TEXT = "BUTTON_TEXT"
    }

    override fun setup() {
        isCancelable = arguments?.getBoolean(IS_DIALOG_CANCELABLE, true) ?: true

        binding.tvTitle.text = arguments?.getString(TITLE_TEXT)
        binding.tvDesc.text = arguments?.getString(MESSAGE_TEXT)
        binding.btnBottomsheet.setButtonText(arguments?.getString(BUTTON_TEXT) ?: "-")

        dialog?.setCanceledOnTouchOutside(false)

        binding.btnBottomsheet.setOnClickListener {
            if (callback != null) {
                callback?.onButtonClicked()
            } else {
                dismiss()
            }
        }
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onButtonClicked()
    }
}