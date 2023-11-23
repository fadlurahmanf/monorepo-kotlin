package com.fadlurahmanf.bebas_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_shared.data.dto.GenderModel
import com.fadlurahmanf.bebas_ui.R

class SelectGenderAdapter : RecyclerView.Adapter<SelectGenderAdapter.ViewHolder>() {
    private var list: List<GenderModel> = listOf(
        GenderModel(
            id = "LAKI-LAKI",
            label = "LAKI-LAKI"
        ),
        GenderModel(
            id = "PEREMPUAN",
            label = "PEREMPUAN"
        )
    )

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById<TextView>(R.id.text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectGenderAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectGenderAdapter.ViewHolder, position: Int) {
        val gender = list[position]
        holder.itemText.text = gender.label
    }

    override fun getItemCount(): Int = list.size
}