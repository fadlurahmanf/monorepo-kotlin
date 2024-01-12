package com.fadlurahmanf.bebas_transaction.presentation.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivitySelectEwalletBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity

class SelectEWalletActivity :
    BaseTransactionActivity<ActivitySelectEwalletBinding>(ActivitySelectEwalletBinding::inflate) {

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {

    }
}