package com.fadlurahmanf.bebas_main.presentation.home.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat

class BankAccountAdapter : RecyclerView.Adapter<BankAccountAdapter.ViewHolder>() {
    private lateinit var context: Context

    private val bankAccounts: ArrayList<HomeBankAccountModel> = arrayListOf()

    fun setList(accounts: List<HomeBankAccountModel>) {
        this.bankAccounts.clear()
        this.bankAccounts.addAll(accounts)
        notifyItemRangeInserted(0, accounts.size)
    }

    private fun hideOrShowAccountBalance(isVisible: Boolean, position: Int) {
        this.bankAccounts[position].isBalanceVisible = isVisible
        notifyItemChanged(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val savingLabel: TextView = view.findViewById(R.id.tv_saving_label_and_account_number)
        val accountBalance: TextView = view.findViewById(R.id.tv_account_balance)
        val pin: ImageView = view.findViewById(R.id.iv_pin)
        val copy: ImageView = view.findViewById(R.id.iv_copy)
        val eye: ImageView = view.findViewById(R.id.iv_account_balance_eye)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_bank_account, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bankAccounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = bankAccounts[position]

        holder.savingLabel.text = "${account.savingTypeLabel} • ${account.accountNumber}"

        if (account.isPinned) {
            holder.pin.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.round_push_pin_24
                )
            )
            holder.pin.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.pinned_color))
        } else {
            holder.pin.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.outline_push_pin_24
                )
            )
            holder.pin.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
        }

        if (account.isBalanceVisible) {
            holder.accountBalance.text =
                account.accountBalance.toRupiahFormat(useSymbol = true, useDecimal = true)
            holder.eye.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.outline_visibility_24
                )
            )
        } else {
            holder.accountBalance.text =
                "Rp *********"
            holder.eye.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.outline_visibility_off_24
                )
            )
        }

        holder.eye.setOnClickListener {
            hideOrShowAccountBalance(!account.isBalanceVisible, position)
        }
    }
}