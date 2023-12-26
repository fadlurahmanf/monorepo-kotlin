package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel

class ContactListAdapter : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar = view.findViewById<TextView>(R.id.tv_initial_avatar)
        val contactName = view.findViewById<TextView>(R.id.tv_contact_name)
        val phoneNumber = view.findViewById<TextView>(R.id.tv_contact_phone_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]

        holder.contactName.text = contact.name
        holder.phoneNumber.text = contact.phoneNumber

        if (contact.name.isNotEmpty()) {
            if (contact.name.contains(" ")) {
                val first = contact.name.split(" ").first().take(1)
                val second = contact.name.split(" ")[1].take(1)
                holder.avatar.text = "$first$second"
            } else {
                holder.avatar.text = contact.name.take(1).uppercase()
            }
        }

        holder.itemView.setOnClickListener {
            callback?.onContactClicked(contact)
        }
    }

    interface Callback {
        fun onContactClicked(contact: BebasContactModel)
    }

}
