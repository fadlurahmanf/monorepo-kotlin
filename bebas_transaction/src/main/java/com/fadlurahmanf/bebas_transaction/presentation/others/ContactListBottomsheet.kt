package com.fadlurahmanf.bebas_transaction.presentation.others

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetContactListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.AlphabetScrollAdapter
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.ContactListAdapter
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ContactListBottomsheet :
    BaseTransactionBottomsheet<BottomsheetContactListBinding>(
        BottomsheetContactListBinding::inflate
    ), AlphabetScrollAdapter.Callback {

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

    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var alphabetAdapter: AlphabetScrollAdapter
    private val contacts: ArrayList<BebasContactModel> = arrayListOf()

    override fun setup() {
        bottomsheetDialog = (dialog as BottomSheetDialog)

        dialog?.setCanceledOnTouchOutside(false)
        bottomsheetDialog.behavior.isDraggable = false
        bottomsheetDialog.behavior.state =
            BottomSheetBehavior.STATE_EXPANDED

        binding.ivBack.setOnClickListener {
            dialog?.dismiss()
        }

        contactListAdapter = ContactListAdapter()
        alphabetAdapter = AlphabetScrollAdapter()
        alphabetAdapter.setCallback(this)
        alphabetAdapter.setAlphabets()
        contactListAdapter.setList(contacts)
        binding.rvContact.adapter = contactListAdapter

        viewModel.contacts.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.SUCCESS -> {
                    contacts.clear()
                    contacts.addAll(it.data)
                    contactListAdapter.setList(contacts)

                    alphabetAdapter.setAlphabets()
                    binding.rvAlphabet.adapter = alphabetAdapter
                }

                else -> {}

            }
        }
        viewModel.getListContact(requireContext())
    }

    interface Callback {
        fun onContactClicked(contact: BebasContactModel)
    }

    override fun onAlphabetTouched(view: View, event: MotionEvent, alphabet: String) {
        Log.d("BebasLogger", "TOUCHED: ${event.action} & ${alphabet}")
    }
}