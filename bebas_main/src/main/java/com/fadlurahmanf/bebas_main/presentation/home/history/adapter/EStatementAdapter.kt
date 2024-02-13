package com.fadlurahmanf.bebas_main.presentation.home.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_shared.helper.BebasSharedHelper

class EStatementAdapter : RecyclerView.Adapter<EStatementAdapter.ViewHolder>() {
    lateinit var context: Context
    private var estatements: ArrayList<EStatementResponse.Statement> = arrayListOf()
    private var callback: Callback? = null

    fun setNewList(list: List<EStatementResponse.Statement>) {
        estatements.clear()
        estatements.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val estatementLabel = view.findViewById<TextView>(R.id.tv_estatement_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_estatement, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = estatements.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estatement = estatements[position]

        holder.estatementLabel.text =
            "${BebasSharedHelper.getFullNameMonth(estatement.month ?: -1)} ${estatement.year ?: -1}"

        holder.itemView.setOnClickListener {
            callback?.onDownloadEstatementClicked(estatement)
        }
    }

    interface Callback {
        fun onDownloadEstatementClicked(estatement: EStatementResponse.Statement)
    }

}
