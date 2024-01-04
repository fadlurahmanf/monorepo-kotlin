package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PulsaDenomModel

class PulsaDenomAdapter : RecyclerView.Adapter<PulsaDenomAdapter.ViewHolder>() {
    lateinit var context: Context
    private var denoms: ArrayList<PulsaDenomModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<PulsaDenomModel>) {
        denoms.clear()
        denoms.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val denom = view.findViewById<TextView>(R.id.tv_denom)
        val total = view.findViewById<TextView>(R.id.tv_total)
        val providerImage = view.findViewById<ImageView>(R.id.iv_logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pulsa_denom, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = denoms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val denom = denoms[position]

        Glide.with(holder.providerImage).load(Uri.parse(denom.providerImage))
            .fallback(ContextCompat.getDrawable(context, R.drawable.il_logo_bebas_grey))
            .into(holder.providerImage)
        holder.denom.text = denom.denom.toRupiahFormat(useDecimal = false)
        holder.total.text = denom.total.toRupiahFormat(useSymbol = true, useDecimal = false)

        holder.itemView.setOnClickListener {
            callback?.onDenomClicked(denom)
        }
    }

    interface Callback {
        fun onDenomClicked(model: PulsaDenomModel)
    }

}
