package com.fadlurahmanf.bebas_ui.bottomsheet

import com.fadlurahmanf.bebas_ui.adapter.BebasPickerBottomsheetAdapter
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding

class BebasPickerBottomsheet : BaseBottomsheet<BottomsheetBebasPickerBinding>(
    BottomsheetBebasPickerBinding::inflate
) {

    private lateinit var adapter: BebasPickerBottomsheetAdapter



    override fun setup() {
        adapter = BebasPickerBottomsheetAdapter()
        binding.rv.adapter = adapter
    }

}