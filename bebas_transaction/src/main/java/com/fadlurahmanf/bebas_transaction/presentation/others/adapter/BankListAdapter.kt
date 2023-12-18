package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

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
import com.bumptech.glide.request.RequestOptions
import com.fadlurahmanf.bebas_api.data.dto.transfer.BankResponse
import com.fadlurahmanf.bebas_transaction.R

class BankListAdapter : RecyclerView.Adapter<BankListAdapter.ViewHolder>() {
    lateinit var context: Context
    private var banks: ArrayList<BankResponse> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<BankResponse>) {
        banks.clear()
        banks.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bankLogo = view.findViewById<ImageView>(R.id.iv_bank_logo)
        val bankName = view.findViewById<TextView>(R.id.tv_bank_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bank, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = banks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bank = banks[position]

        holder.bankName.text = bank.nickName
        Glide.with(holder.bankLogo).applyDefaultRequestOptions(
            RequestOptions().error(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.round_account_balance_24
                )
            ).fallback(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.round_account_balance_24
                )
            ).placeholder(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.round_account_balance_24
                )
            )
        ).load(Uri.parse(bank.image ?: "")).into(holder.bankLogo)

        holder.itemView.setOnClickListener {
            callback?.onItemClicked(bank)
        }
    }

    interface Callback {
        fun onItemClicked(bank: BankResponse)
    }

}
