package com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.flow.favorite.LatestAdapterFlow
import com.fadlurahmanf.bebas_transaction.external.BebasTransactionHelper

class LatestAdapter(private val flow: LatestAdapterFlow) :
    RecyclerView.Adapter<LatestAdapter.ViewHolder>() {
    lateinit var context: Context
    private var latests: ArrayList<LatestTransactionModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<LatestTransactionModel>) {
        val itemCount = latests.size
        latests.clear()
        if (itemCount > 0) {
            notifyItemRangeRemoved(0, itemCount)
        }

        latests.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initialAvatar = view.findViewById<TextView>(R.id.initialAvatar)
        val latestLogo = view.findViewById<ImageView>(R.id.iv_latest_logo)
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

        holder.favoriteName.text = latest.label
        if (latest.label.isNotEmpty()) {
            holder.initialAvatar.text = BebasTransactionHelper.getInitial(latest.label)
        }

        holder.subFavoriteLavel.text = latest.subLabel

        when (flow) {
            LatestAdapterFlow.PULSA_PREPAID -> {
                if (latest.additionalPulsaPrePaid?.providerLogo != null) {
                    Glide.with(holder.latestLogo)
                        .load(Uri.parse(latest.additionalPulsaPrePaid.providerLogo ?: ""))
                        .into(holder.latestLogo)
                }

                holder.latestLogo.visibility = View.VISIBLE
                holder.initialAvatar.visibility = View.GONE
            }

            else -> {
                holder.latestLogo.visibility = View.GONE
                holder.initialAvatar.visibility = View.VISIBLE
            }
        }

        holder.itemView.setOnClickListener {
            callback?.onLatestTransactionItemClicked(latest)
        }
    }

    interface Callback {
        fun onLatestTransactionItemClicked(latest: LatestTransactionModel)
    }

}
