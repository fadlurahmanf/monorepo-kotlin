package com.fadlurahmanf.mapp_ui.presentation.bottomsheet

import android.view.View
import com.fadlurahmanf.mapp_ui.databinding.BottomsheetInfoBinding

class InfoBottomsheet : BaseBottomsheet<BottomsheetInfoBinding>(
    BottomsheetInfoBinding::inflate
) {

    companion object {
        const val IS_DIALOG_CANCELABLE = "IS_DIALOG_CANCELABLE"
        const val DRAWABLE_RES = "DRAWABLE_RES"
        const val TITLE_TEXT = "TITLE_TEXT"
        const val DESC_TEXT = "DESC_TEXT"
        const val BUTTON_TEXT = "BUTTON_TEXT"
    }

    override fun setup() {
        isCancelable = arguments?.getBoolean(IS_DIALOG_CANCELABLE, true) ?: true
        val asset = arguments?.getInt(DRAWABLE_RES)
        if (asset != null) {
            binding.ivAsset.setImageResource(asset)
        } else {
            binding.ivAsset.visibility = View.GONE
        }

        binding.tvTitle.text = arguments?.getString(TITLE_TEXT)
        binding.tvDesc.text = arguments?.getString(DESC_TEXT)
        binding.btnWdgt.text = arguments?.getString(BUTTON_TEXT) ?: "-"

        dialog?.setCanceledOnTouchOutside(false)
    }

    interface Callback {

    }
}