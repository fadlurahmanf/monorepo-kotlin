package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel

class ContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    private var contacts: ArrayList<BebasContactModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<BebasContactModel>) {
        contacts.clear()
        contacts.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar = view.findViewById<TextView>(R.id.tv_initial_avatar)
        val contactName = view.findViewById<TextView>(R.id.tv_contact_name)
        val phoneNumber = view.findViewById<TextView>(R.id.tv_contact_phone_number)
    }

    class IndicatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val indicator = view.findViewById<TextView>(R.id.tv_alphabet_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        return if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
            ContactViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_indicator_alphabet_contact, parent, false)
            IndicatorViewHolder(view)
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = contacts[position]

        if (getItemViewType(position) == 1) {
            val mHolder = holder as ContactViewHolder
            mHolder.contactName.text = contact.name
            mHolder.phoneNumber.text = contact.phoneNumber

            if (contact.name.isNotEmpty()) {
                if (contact.name.contains(" ")) {
                    val first = contact.name.split(" ").first().take(1)
                    val second = contact.name.split(" ")[1].take(1)
                    mHolder.avatar.text = "$first$second"
                } else {
                    mHolder.avatar.text = contact.name.take(1).uppercase()
                }
            }

            mHolder.itemView.setOnClickListener {
                callback?.onContactClicked(contact)
            }
        } else {
            val mHolder = holder as IndicatorViewHolder
            mHolder.indicator.text = contact.name
        }
    }

    interface Callback {
        fun onContactClicked(contact: BebasContactModel)
    }

    override fun getItemViewType(position: Int): Int {
        return contacts[position].type
    }

}
