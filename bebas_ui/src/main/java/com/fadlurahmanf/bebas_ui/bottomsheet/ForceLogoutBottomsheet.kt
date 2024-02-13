package com.fadlurahmanf.bebas_ui.bottomsheet

import com.fadlurahmanf.bebas_ui.databinding.BottomsheetForceLogoutBinding

class ForceLogoutBottomsheet : BaseBottomsheet<BottomsheetForceLogoutBinding>(
    BottomsheetForceLogoutBinding::inflate
) {

    private var callback: Callback? = null

    override fun setup() {
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

        binding.btnBottomsheet.setOnClickListener {
            if (callback != null) {
                callback?.onButtonClicked()
            } else if (isCancelable) {
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