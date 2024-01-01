package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.external.BebasTransactionHelper
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel

class ContactListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    private lateinit var initialContacts: List<BebasContactModel>
    private var contacts: ArrayList<BebasContactModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<BebasContactModel>) {
        contacts.clear()
        initialContacts = list
        contacts.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun refreshListByKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            val itemCount = contacts.size
            contacts.clear()
            notifyItemRangeRemoved(0, itemCount)
            contacts.addAll(initialContacts)
            notifyItemRangeInserted(0, initialContacts.size)
        } else {
            val itemCount = contacts.size
            contacts.clear()
            notifyItemRangeRemoved(0, itemCount)
            val newContacts = arrayListOf<BebasContactModel>()
            val newAlphabetIndicator = arrayListOf<String>()
            val initialContactsWithIndicator = initialContacts.filter {
                it.type == 1
            }
            for (element in initialContactsWithIndicator) {
                if (element.name.contains(
                        keyword,
                        ignoreCase = true
                    ) || element.phoneNumber.contains(keyword, ignoreCase = true)
                ) {
                    val indicator = element.name.take(1)
                    if (!newAlphabetIndicator.contains(indicator)) {
                        newAlphabetIndicator.add(indicator)
                        newContacts.add(
                            BebasContactModel(
                                name = indicator,
                                nameHtml = indicator,
                                phoneNumber = indicator,
                                phoneNumberHtml = indicator,
                                type = 0
                            )
                        )
                    }
                    val newElement = element.copy(
                        nameHtml = element.name.replace(
                            keyword,
                            "<font color='red'>$keyword</font>",
                            ignoreCase = true
                        ),
                        phoneNumber = element.phoneNumber.replace(
                            keyword, "<font color='red'>$keyword</font>",
                            ignoreCase = true
                        )
                    )
                    newContacts.add(newElement)
                }
            }
            contacts.addAll(newContacts)
            notifyItemRangeInserted(0, newContacts.size)
        }
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
            mHolder.contactName.text = Html.fromHtml(contact.nameHtml)
            mHolder.phoneNumber.text = Html.fromHtml(contact.phoneNumberHtml)

            if (contact.name.isNotEmpty()) {
                mHolder.avatar.text = BebasTransactionHelper.getInitial(contact.name)
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
