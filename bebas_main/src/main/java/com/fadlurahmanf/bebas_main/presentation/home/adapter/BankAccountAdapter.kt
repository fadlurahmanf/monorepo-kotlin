package com.fadlurahmanf.bebas_main.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_shared.extension.toRupiahFormat

class BankAccountAdapter : RecyclerView.Adapter<BankAccountAdapter.ViewHolder>() {

    private val bankAccounts: ArrayList<BankAccountResponse> = arrayListOf()

    fun setList(accounts: List<BankAccountResponse>) {
        this.bankAccounts.clear()
        this.bankAccounts.addAll(accounts)
        notifyItemRangeInserted(0, accounts.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val savingLabel: TextView = view.findViewById(R.id.tv_saving_label_and_account_number)
        val accountBalance: TextView = view.findViewById(R.id.tv_account_balance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_bank_account, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = bankAccounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = bankAccounts[position]

        holder.savingLabel.text = "MAS Saving • ${account.accountNumber}"
        holder.accountBalance.text =
            account.workingBalance?.toRupiahFormat(useSymbol = true, useDecimal = true)
    }
}