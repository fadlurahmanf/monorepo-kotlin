package com.fadlurahmanf.bebas_loyalty.presentation.history

import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.bebas_loyalty.databinding.ActivityHistoryLoyaltyBinding
import com.fadlurahmanf.bebas_loyalty.presentation.BaseLoyaltyActivity
import com.fadlurahmanf.bebas_loyalty.presentation.history.adapter.LoyaltyHistoryPagingAdapter
import javax.inject.Inject

class HistoryLoyaltyActivity :
    BaseLoyaltyActivity<ActivityHistoryLoyaltyBinding>(ActivityHistoryLoyaltyBinding::inflate) {

    @Inject
    lateinit var viewModel: HistoryLoyaltyViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: LoyaltyHistoryPagingAdapter

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        viewModel.notificationState.observe(this) {
            Log.d("BebasLogger", "PAGING DATA LOADED: $it")
            adapter.submitData(lifecycle, it)
        }

        adapter = LoyaltyHistoryPagingAdapter()
        binding.rvHistory.adapter = adapter

        viewModel.getNotification(applicationContext)
    }

}