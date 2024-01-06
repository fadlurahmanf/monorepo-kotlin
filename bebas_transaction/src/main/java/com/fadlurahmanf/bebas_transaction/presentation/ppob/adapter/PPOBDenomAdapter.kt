package com.fadlurahmanf.bebas_transaction.presentation.ppob.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_shared.extension.toDp
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.data.flow.PPOBDenomFlow

class PPOBDenomAdapter : RecyclerView.Adapter<PPOBDenomAdapter.ViewHolder>() {
    lateinit var context: Context
    private var denoms: ArrayList<PPOBDenomModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<PPOBDenomModel>) {
        denoms.clear()
        denoms.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.findViewById<LinearLayout>(R.id.ll_main)
        val denomLogo = view.findViewById<ImageView>(R.id.iv_denom_logo)
        val nominal = view.findViewById<TextView>(R.id.tv_nominal)
        val totalBayar = view.findViewById<TextView>(R.id.tv_total_bayar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ppob_denom, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = denoms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val denom = denoms[position]

        if (position % 2 == 0) {
            val params = holder.llMain.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 10.toDp(), 5.toDp(), 0)
            holder.llMain.layoutParams = params
        } else {
            val params = holder.llMain.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(5.toDp(), 10.toDp(), 0, 0)
            holder.llMain.layoutParams = params
        }

        if (denom.imageUrl != null) {
            Glide.with(holder.denomLogo).load(Uri.parse(denom.imageUrl ?: ""))
                .into(holder.denomLogo)
        } else {
            when (denom.flow) {
                PPOBDenomFlow.PLN_PREPAID -> {
                    val params = holder.denomLogo.layoutParams as ViewGroup.LayoutParams
                    params.width = 70.toDp()
                    params.height = 30.toDp()
                    holder.denomLogo.layoutParams = params
                    holder.denomLogo.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.il_logo_pln_denom
                        )
                    )
                }

                else -> {

                }
            }
        }
        holder.nominal.text = denom.nominal.toRupiahFormat(useDecimal = false)
        holder.totalBayar.text =
            denom.totalBayar.toRupiahFormat(useSymbol = true, useDecimal = false)

        holder.itemView.setOnClickListener {
            callback?.onDenomClicked(denom)
        }
    }

    interface Callback {
        fun onDenomClicked(model: PPOBDenomModel)
    }

}
