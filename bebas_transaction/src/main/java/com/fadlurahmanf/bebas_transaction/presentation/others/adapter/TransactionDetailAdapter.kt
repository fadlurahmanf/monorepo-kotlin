package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.model.TransactionDetailModel

class TransactionDetailAdapter :
    RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder>() {
    lateinit var context: Context
    private var details: ArrayList<TransactionDetailModel> = arrayListOf()
    fun setList(list: List<TransactionDetailModel>) {
        details.clear()
        details.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun resetList(list: List<TransactionDetailModel>) {
        val itemCount = details.size
        details.clear()
        notifyItemRangeRemoved(0, itemCount)
        details.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.findViewById<TextView>(R.id.tv_label)
        val value = view.findViewById<TextView>(R.id.tv_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_label_value, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = details.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detail = details[position]

        holder.label.text = detail.label
        holder.value.text = detail.value

        if (detail.valueStyle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.value.setTextAppearance(detail.valueStyle)
            } else {
                holder.value.setTextAppearance(context, detail.valueStyle)
            }
        }
    }

}
