package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.os.Bundle
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPaymentDetailBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import javax.inject.Inject

class PaymentDetailActivity :
    BaseTransactionActivity<ActivityPaymentDetailBinding>(ActivityPaymentDetailBinding::inflate) {

    @Inject
    lateinit var viewModel: PaymentDetailViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {

    }
}