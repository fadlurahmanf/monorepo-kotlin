package com.fadlurahmanf.bebas_transaction.presentation.others

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.databinding.BottomsheetContactListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.AlphabetScrollAdapter
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.ContactListAdapter
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class ContactListBottomsheet :
    BaseTransactionBottomsheet<BottomsheetContactListBinding>(
        BottomsheetContactListBinding::inflate
    ), AlphabetScrollAdapter.Callback, ContactListAdapter.Callback {

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

    private lateinit var contactLayoutManager: LinearLayoutManager
    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var alphabetAdapter: AlphabetScrollAdapter
    private val contacts: ArrayList<BebasContactModel> = arrayListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun setup() {
        bottomsheetDialog = (dialog as BottomSheetDialog)

        dialog?.setCanceledOnTouchOutside(false)
        bottomsheetDialog.behavior.isDraggable = false
        bottomsheetDialog.behavior.state =
            BottomSheetBehavior.STATE_EXPANDED

        binding.ivBack.setOnClickListener {
            dialog?.dismiss()
        }

        contactLayoutManager = LinearLayoutManager(context)
        contactListAdapter = ContactListAdapter()
        contactListAdapter.setCallback(this)
        alphabetAdapter = AlphabetScrollAdapter()
        alphabetAdapter.setCallback(this)
        alphabetAdapter.setAlphabets(alphabets)
        contactListAdapter.setList(contacts)
        binding.rvContact.layoutManager = contactLayoutManager
        binding.rvContact.adapter = contactListAdapter

        binding.etNameOrPhone.addTextChangedListener(object :
                                                         BebasEdittext.BebasEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                contactListAdapter.refreshListByKeyword(s.toString())
            }
        })


        binding.rvAlphabet.setOnTouchListener { v, event ->
            val indexAlphabet = getIndexAlphabet(event.y)
            Log.d("BebasLogger", "TOUCHED: ${v.y} & ${event.y}")
            Log.d("BebasLogger", "MASUK INDEX ALPHABET $indexAlphabet")
            Log.d("BebasLogger", "MASUK ALPHABETS LENGTH ${alphabets.size}")
            val alphabet = alphabets[indexAlphabet]
            Log.d("BebasLogger", "MASUK ALPHABET $alphabet")
            val indexStarOfAlphabet = getStartIndexOfAlphabetsInContacts(alphabet)
            Log.d("BebasLogger", "MASUK INDEX CONTACT $indexStarOfAlphabet")

            Log.d("BebasLogger", "MASUK CONTACT ${contacts[indexStarOfAlphabet]}")
            binding.tvAlphabetSelected.text = alphabet.uppercase()
            resetViewAlphabetSelected()
            contactLayoutManager.scrollToPositionWithOffset(indexStarOfAlphabet, 20)
            false
        }

        viewModel.contacts.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {

                }

                is NetworkState.SUCCESS -> {
                    contacts.clear()
                    contacts.addAll(it.data)
                    contactListAdapter.setList(contacts)

                    binding.rvAlphabet.adapter = alphabetAdapter
                }

                else -> {}

            }
        }
        viewModel.getListContact(requireContext())
    }

    private val handler = Handler(Looper.getMainLooper())
    private val alphabetSelectedRunnable =
        Runnable { binding.tvAlphabetSelected.visibility = View.GONE }

    private fun resetViewAlphabetSelected() {
        handler.removeCallbacks(alphabetSelectedRunnable)
        binding.tvAlphabetSelected.visibility = View.VISIBLE
        handler.postDelayed(alphabetSelectedRunnable, 1500)
    }

    interface Callback {
        fun onContactClicked(contact: BebasContactModel)
    }

    override fun onAlphabetTouched(view: View, event: MotionEvent, alphabet: String) {
        Log.d("BebasLogger", "TOUCHED: ${event.action} & ${alphabet}")
    }

    override fun onContactClicked(contact: BebasContactModel) {
        dialog?.dismiss()
        callback?.onContactClicked(contact)
    }

    private fun getIndexAlphabet(y: Float): Int {
        return try {
            val indexAlphabet = ((y / 50) - 0.5f).toInt()
            if (indexAlphabet < 0) {
                0
            } else if (indexAlphabet > 24) {
                24
            } else {
                indexAlphabet
            }
        } catch (e: Throwable) {
            0
        }
    }

    private fun getStartIndexOfAlphabetsInContacts(alphabet: String): Int {
        var index: Int = 0
        for (element in 0 until contacts.size) {
            index = element
            if (contacts[element].name.startsWith(alphabet)) {
                break
            }
        }
        return index
    }

    private val alphabets = arrayListOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )
}