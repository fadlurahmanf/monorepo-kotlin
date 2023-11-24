package com.fadlurahmanf.bebas_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_ui.R

class BebasPickerBottomsheetAdapter() :
    RecyclerView.Adapter<BebasPickerBottomsheetAdapter.ViewHolder>() {

    private var list: ArrayList<BebasItemPickerBottomsheetModel> = arrayListOf()

    private lateinit var callback: Callback

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setList(list: ArrayList<BebasItemPickerBottomsheetModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun refreshList(list: ArrayList<BebasItemPickerBottomsheetModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

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
        val model = list[position]
        holder.itemText.text = model.label

        holder.itemView.setOnClickListener {
            callback.onItemClicked(model)
        }
    }

    override fun getItemCount(): Int = list.size

    interface Callback {
        fun onItemClicked(model: BebasItemPickerBottomsheetModel)
    }
}