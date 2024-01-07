package com.fadlurahmanf.bebas_transaction.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel

class PaymentSourceAdapter : RecyclerView.Adapter<PaymentSourceAdapter.ViewHolder>() {
    lateinit var context: Context
    private var paymentSources: ArrayList<PaymentSourceModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<PaymentSourceModel>) {
        paymentSources.clear()
        paymentSources.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val accountName = view.findViewById<TextView>(R.id.tv_account_name)
        val subLabel = view.findViewById<TextView>(R.id.tv_saving_type_and_account_number)
        val accountBalance = view.findViewById<TextView>(R.id.tv_account_balance)
        val drawableEnd = view.findViewById<ImageView>(R.id.iv_drawable_end)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_source, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = paymentSources.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paymentSource = paymentSources[position]

        holder.accountName.text = paymentSource.accountName
        holder.subLabel.text = paymentSource.subLabel
        holder.accountBalance.text =
            paymentSource.balance.toRupiahFormat(useSymbol = true, useDecimal = false)
        holder.drawableEnd.visibility = View.GONE

        holder.itemView.setOnClickListener {
            callback?.onSelectPaymentSource(paymentSource)
        }
    }

    interface Callback {
        fun onSelectPaymentSource(bank: PaymentSourceModel)
    }

}
