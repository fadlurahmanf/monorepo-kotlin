package com.fadlurahmanf.bebas_loyalty.presentation.history

import android.os.Bundle
import com.fadlurahmanf.bebas_loyalty.databinding.ActivityHistoryLoyaltyBinding
import com.fadlurahmanf.bebas_loyalty.presentation.BaseLoyaltyActivity
import javax.inject.Inject

class HistoryLoyaltyActivity : BaseLoyaltyActivity<ActivityHistoryLoyaltyBinding>(ActivityHistoryLoyaltyBinding::inflate) {

    @Inject
    lateinit var viewModel:HistoryLoyaltyViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {

    }

}