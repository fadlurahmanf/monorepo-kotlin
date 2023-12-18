package com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.LatestTransactionModel

class LatestAdapter : RecyclerView.Adapter<LatestAdapter.ViewHolder>() {
    lateinit var context: Context
    private var latests: ArrayList<LatestTransactionModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<LatestTransactionModel>) {
        latests.clear()
        latests.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initialAvatar = view.findViewById<TextView>(R.id.initialAvatar)
        val favoriteName = view.findViewById<TextView>(R.id.tv_latest_name)
        val subFavoriteLavel = view.findViewById<TextView>(R.id.tv_latest_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_latest, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = latests.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val latest = latests[position]

        holder.favoriteName.text = latest.name
        if (latest.name.isNotEmpty()) {
            if (latest.name.contains(" ")) {
                val first = latest.name.split(" ").first().take(1)
                val second = latest.name.split(" ")[1].take(1)

                holder.initialAvatar.text = "$first$second"
            } else {
                holder.initialAvatar.text = latest.name.split("").first()
            }
        }

        holder.subFavoriteLavel.text = latest.labelLatest

        holder.itemView.setOnClickListener {
            callback?.onItemClicked(latest)
        }
    }

    interface Callback {
        fun onItemClicked(latest: LatestTransactionModel)
    }

}