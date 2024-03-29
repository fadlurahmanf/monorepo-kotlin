package com.fadlurahmanf.bebas_transaction.presentation.others

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
import com.fadlurahmanf.bebas_transaction.data.dto.argument.TransferDetailArgument
import com.fadlurahmanf.bebas_transaction.data.flow.InputDestinationAccountFlow
import com.fadlurahmanf.bebas_transaction.data.flow.TransferDetailFlow
import com.fadlurahmanf.bebas_transaction.data.state.BankListState
import com.fadlurahmanf.bebas_transaction.data.state.InquiryState
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

                else -> {}
            }
        }

        viewModel.inquiryState.observe(this) {
            when (it) {
                is InquiryState.FailedBebas -> {
                    dismissLoadingDialog()
                    showFailedBebasBottomsheet(it.exception)
                }

                InquiryState.LOADING -> {
                    showLoadingDialog()
                }

                is InquiryState.SuccessFromListActivity -> {
                    dismissLoadingDialog()
                    goToTransferDetailAfterInquiry(
                        data = it.result,
                        destinationAccountNumber = it.destinationAccount,
                        selectedBank = it.selectedBank,
                        isInquiryBankMas = it.isInquiryBankMas
                    )
                }

                else -> {

                }
            }
        }

        viewModel.getBankList()
    }

    private fun goToTransferDetailAfterInquiry(
        data: InquiryBankResponse,
        destinationAccountNumber: String,
        selectedBank: ItemBankResponse,
        isInquiryBankMas: Boolean
    ) {
        val intent = Intent(this, TransferDetailActivity::class.java)
        intent.putExtra(
            TransferDetailActivity.FLOW,
            if (isInquiryBankMas) TransferDetailFlow.TRANSFER_BETWEEN_BANK_MAS.name else TransferDetailFlow.TRANSFER_BETWEEN_BANK_MAS.name
        )
        intent.putExtra(
            TransferDetailActivity.ARGUMENT,
            TransferDetailArgument(
                isFavorite = false,
                accountName = data.destinationAccountName ?: "-",
                realAccountName = data.destinationAccountName ?: "-",
                accountNumber = destinationAccountNumber,
                bankImageUrl = selectedBank.image,
                bankName = selectedBank.name ?: "-",
                inquiryBank = data
            )
        )
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
                    viewModel.inquiryBankMas(destinationAccount, bank)
                } else {
                    viewModel.inquiryOtherBank(bank.sknId ?: "-", destinationAccount, bank)
                }
            }
        })
        inputDestinationAccountBottomsheet?.arguments = Bundle().apply {
            putString(
                InputDestinationAccountBottomsheet.FLOW,
                InputDestinationAccountFlow.TRANSFER.name
            )
            putString(InputDestinationAccountBottomsheet.LABEL_NEW_RECEIVER, bank.nickName ?: "-")
            putString(InputDestinationAccountBottomsheet.IMAGE_NEW_RECEIVER_LOGO, bank.image ?: "-")
        }
        inputDestinationAccountBottomsheet?.show(
            supportFragmentManager,
            InputDestinationAccountBottomsheet::class.java.simpleName
        )
    }

}