package com.fadlurahmanf.bebas_transaction.presentation.others

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.transfer.BankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityBankListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.BankListAdapter
import com.fadlurahmanf.bebas_transaction.presentation.transfer.DestinationBankAccountBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import javax.inject.Inject

class BankListActivity :
    BaseTransactionActivity<ActivityBankListBinding>(ActivityBankListBinding::inflate),
    BankListAdapter.Callback, DestinationBankAccountBottomsheet.Callback {

    @Inject
    lateinit var viewModel: BankListViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: BankListAdapter
    private val banks: ArrayList<BankResponse> = arrayListOf()

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        adapter = BankListAdapter()
        adapter.setCallback(this)
        binding.rvBankList.adapter = adapter
        viewModel.bankListState.observe(this) {
            when (it) {
                is NetworkState.LOADING -> {
                    binding.llOtherBank.visibility = View.GONE
                }

                is NetworkState.FAILED -> {
                    binding.llOtherBank.visibility = View.GONE
                }

                is NetworkState.SUCCESS -> {
                    banks.clear()
                    banks.addAll(it.data)
                    adapter.setList(banks)

                    binding.llOtherBank.visibility = View.VISIBLE
                }

                else -> {

                }
            }
        }

        viewModel.inquiryBankMasState.observe(this) {
            when (it) {
                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    goToTransferDetailAfterInquiry(it.data)
                }

                else -> {

                }
            }
        }

        viewModel.getBankList()
    }

    private fun goToTransferDetailAfterInquiry(data: InquiryBankResponse) {
        val intent = Intent(this, TransferDetailActivity::class.java)
        startActivity(intent)
    }

    private var destinationBankAccountBottomsheet: DestinationBankAccountBottomsheet? = null
    override fun onItemClicked(bank: BankResponse) {
        destinationBankAccountBottomsheet = DestinationBankAccountBottomsheet()
        destinationBankAccountBottomsheet?.setCallback(this)
        destinationBankAccountBottomsheet?.arguments = Bundle().apply {
            putString(DestinationBankAccountBottomsheet.BANK_NAME, bank.nickName ?: "-")
            putString(DestinationBankAccountBottomsheet.BANK_IMAGE, bank.image ?: "-")
        }
        destinationBankAccountBottomsheet?.show(
            supportFragmentManager,
            DestinationBankAccountBottomsheet::class.java.simpleName
        )
    }

    override fun onNextClicked(dialog: Dialog?, accountBankNumber: String) {
        super.onNextClicked(dialog, accountBankNumber)
        viewModel.inquiryBankMas(accountBankNumber)
    }

}