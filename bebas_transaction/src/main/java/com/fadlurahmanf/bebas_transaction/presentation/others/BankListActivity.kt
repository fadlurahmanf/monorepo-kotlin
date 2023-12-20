package com.fadlurahmanf.bebas_transaction.presentation.others

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.transfer.ItemBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.data.flow.InputDestinationAccountFlow
import com.fadlurahmanf.bebas_transaction.data.state.BankListState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityBankListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.BankListAdapter
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import javax.inject.Inject

class BankListActivity :
    BaseTransactionActivity<ActivityBankListBinding>(ActivityBankListBinding::inflate),
    BankListAdapter.Callback {

    @Inject
    lateinit var viewModel: BankListViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var otherBanksAdapter: BankListAdapter
    private val otherBanks: ArrayList<ItemBankResponse> = arrayListOf()
    private lateinit var topBanksAdapter: BankListAdapter
    private val topBanks: ArrayList<ItemBankResponse> = arrayListOf()

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        otherBanksAdapter = BankListAdapter()
        otherBanksAdapter.setCallback(this)
        topBanksAdapter = BankListAdapter()
        topBanksAdapter.setCallback(this)
        binding.rvOtherBanks.adapter = otherBanksAdapter
        binding.rvTopBanks.adapter = topBanksAdapter
        viewModel.bankListState.observe(this) {
            when (it) {
                is BankListState.LOADING -> {
                    binding.llTopBanks.visibility = View.GONE
                    binding.llOtherBanks.visibility = View.GONE
                }

                is BankListState.FAILED -> {
                    binding.llTopBanks.visibility = View.GONE
                    binding.llOtherBanks.visibility = View.GONE
                }

                is BankListState.SUCCESS -> {
                    topBanks.clear()
                    topBanks.addAll(it.topBanks)
                    topBanksAdapter.setList(topBanks)

                    otherBanks.clear()
                    otherBanks.addAll(it.otherBanks)
                    otherBanksAdapter.setList(otherBanks)

                    if (topBanks.isNotEmpty()) {
                        binding.llTopBanks.visibility = View.VISIBLE
                    }

                    if (otherBanks.isNotEmpty()) {
                        binding.llOtherBanks.visibility = View.VISIBLE
                    }
                }

                else -> {

                }
            }
        }

        viewModel.inquiryBankState.observe(this) {
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

    private var inputDestinationAccountBottomsheet: InputDestinationAccountBottomsheet? = null
    override fun onItemClicked(bank: ItemBankResponse) {
        inputDestinationAccountBottomsheet = InputDestinationAccountBottomsheet()
        inputDestinationAccountBottomsheet?.setCallback(object :
                                                            InputDestinationAccountBottomsheet.Callback {
            override fun onNextClicked(dialog: Dialog?, destinationAccount: String) {
                super.onNextClicked(dialog, destinationAccount)
                if (bank.rtgsId == "BMSEIDJA" && bank.sknId == "5480300") {
                    viewModel.inquiryBankMas(destinationAccount)
                } else {
                    viewModel.inquiryOtherBank(bank.sknId ?: "-", destinationAccount)
                }
            }
        })
        inputDestinationAccountBottomsheet?.arguments = Bundle().apply {
            putString(
                InputDestinationAccountBottomsheet.FLOW,
                InputDestinationAccountFlow.TRANSFER.name
            )
            putString(InputDestinationAccountBottomsheet.LABEL_NAME, bank.nickName ?: "-")
            putString(InputDestinationAccountBottomsheet.IMAGE_LOGO_URL, bank.image ?: "-")
        }
        inputDestinationAccountBottomsheet?.show(
            supportFragmentManager,
            InputDestinationAccountBottomsheet::class.java.simpleName
        )
    }

}