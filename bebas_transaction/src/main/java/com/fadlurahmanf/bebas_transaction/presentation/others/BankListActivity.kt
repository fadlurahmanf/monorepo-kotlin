package com.fadlurahmanf.bebas_transaction.presentation.others

import android.os.Bundle
import android.view.View
import com.fadlurahmanf.bebas_api.data.dto.transfer.BankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_transaction.databinding.ActivityBankListBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_transaction.presentation.others.adapter.BankListAdapter
import javax.inject.Inject

class BankListActivity :
    BaseTransactionActivity<ActivityBankListBinding>(ActivityBankListBinding::inflate),
    BankListAdapter.Callback {

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

        viewModel.getBankList()
    }

    override fun onItemClicked(latest: BankResponse) {

    }

}