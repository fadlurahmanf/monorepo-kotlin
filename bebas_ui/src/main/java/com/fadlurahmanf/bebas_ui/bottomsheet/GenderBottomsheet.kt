package com.fadlurahmanf.bebas_ui.bottomsheet

import com.fadlurahmanf.bebas_ui.adapter.SelectGenderAdapter
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetGenderBinding

class GenderBottomsheet : BaseBottomsheet<BottomsheetGenderBinding>(
    BottomsheetGenderBinding::inflate
) {

    companion object {
        const val IS_DIALOG_CANCELABLE = "IS_DIALOG_CANCELABLE"
        const val DRAWABLE_RES = "DRAWABLE_RES"
        const val TITLE_TEXT = "TITLE_TEXT"
        const val MESSAGE_TEXT = "callback?.onButtonClicked()"
        const val BUTTON_TEXT = "BUTTON_TEXT"
    }

    private lateinit var adapter: SelectGenderAdapter

    override fun setup() {
        adapter = SelectGenderAdapter()
        binding.rv.adapter = adapter
    }

}