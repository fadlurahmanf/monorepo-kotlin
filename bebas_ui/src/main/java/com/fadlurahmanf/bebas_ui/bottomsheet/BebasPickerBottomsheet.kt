package com.fadlurahmanf.bebas_ui.bottomsheet

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.fadlurahmanf.bebas_shared.R
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_ui.adapter.BebasPickerBottomsheetAdapter
import com.fadlurahmanf.bebas_ui.databinding.BottomsheetBebasPickerBinding

class BebasPickerBottomsheet(
    private val editTextHint: String,
    content: List<BebasItemPickerBottomsheetModel>,
    private val callback: BebasPickerBottomsheetAdapter.Callback
) :
    BaseBottomsheet<BottomsheetBebasPickerBinding>(
        BottomsheetBebasPickerBinding::inflate
    ) {

    private lateinit var adapter: BebasPickerBottomsheetAdapter

    private var initialList: List<BebasItemPickerBottomsheetModel> = content
    private var searchedList: ArrayList<BebasItemPickerBottomsheetModel> = ArrayList(initialList)

    override fun setup() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val searchedText = s.toString()
                searchedList.clear()
                initialList.forEach { model ->
                    if (model.label.contains(searchedText, true)) {
                        searchedList.add(model)
                    }
                }
                adapter.refreshList(searchedList)

                if (searchedList.isEmpty()) {
                    binding.llEmptyState.visibility = View.VISIBLE
                    binding.tvEmptyText.text =
                        getString(R.string.empty_search_picker_bottomsheet, editTextHint)
                } else {
                    binding.llEmptyState.visibility = View.GONE
                }
            }

        })

        binding.etSearch.hint = editTextHint

        adapter = BebasPickerBottomsheetAdapter()
        adapter.setList(searchedList)
        adapter.setCallback(callback)
        binding.rv.adapter = adapter
    }

}