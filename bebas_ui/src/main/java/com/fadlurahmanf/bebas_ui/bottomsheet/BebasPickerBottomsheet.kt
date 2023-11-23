package com.fadlurahmanf.bebas_ui.bottomsheet

import android.util.Log
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_ui.adapter.BebasPickerBottomsheetAdapter
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding

class BebasPickerBottomsheet(private val list: List<BebasItemPickerBottomsheetModel>) :
    BaseBottomsheet<BottomsheetBebasPickerBinding>(
        BottomsheetBebasPickerBinding::inflate
    ) {

    private lateinit var adapter: BebasPickerBottomsheetAdapter

    override fun setup() {
        adapter = BebasPickerBottomsheetAdapter(list)
        binding.rv.adapter = adapter
    }

}