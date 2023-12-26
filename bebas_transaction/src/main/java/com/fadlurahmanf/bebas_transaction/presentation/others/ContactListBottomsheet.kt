package com.fadlurahmanf.bebas_transaction.presentation.others

import android.util.Log
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetContactListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.ContactListAdapter
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ContactListBottomsheet :
    BaseTransactionBottomsheet<BottomsheetContactListBinding>(
        BottomsheetContactListBinding::inflate
    ) {

    @Inject
    lateinit var viewModel: ContactListViewModel

    private var destinationPhoneNumber: String? = null

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    private lateinit var bottomsheetDialog: BottomSheetDialog
    override fun injectBottomsheet() {
        component.inject(this)
    }

    private lateinit var adapter: ContactListAdapter
    private val contacts: ArrayList<BebasContactModel> = arrayListOf()

    override fun setup() {
        bottomsheetDialog = (dialog as BottomSheetDialog)

        dialog?.setCanceledOnTouchOutside(false)
        bottomsheetDialog.behavior.state =
            BottomSheetBehavior.STATE_EXPANDED

        binding.ivBack.setOnClickListener {
            dialog?.dismiss()
        }

        adapter = ContactListAdapter()
        adapter.setList(contacts)
        binding.rvContact.adapter = adapter

        viewModel.contacts.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.SUCCESS -> {
                    contacts.clear()
                    contacts.addAll(it.data)
                    adapter.setList(contacts)
                }

                else -> {}

            }
        }
        viewModel.getListContact(requireContext())
    }

    interface Callback {
        fun onContactClicked(contact: BebasContactModel)
    }
}