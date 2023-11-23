package com.fadlurahmanf.bebas_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_ui.R

class BebasPickerBottomsheetAdapter(
    private val list:List<BebasItemPickerBottomsheetModel>
) : RecyclerView.Adapter<BebasPickerBottomsheetAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById<TextView>(R.id.text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BebasPickerBottomsheetAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bebas_item_picker_text, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BebasPickerBottomsheetAdapter.ViewHolder, position: Int) {
        val gender = list[position]
        holder.itemText.text = gender.label
    }

    override fun getItemCount(): Int = list.size
}