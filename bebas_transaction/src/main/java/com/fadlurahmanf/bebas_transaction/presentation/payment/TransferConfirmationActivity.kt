package com.fadlurahmanf.bebas_transaction.presentation.payment

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityTransferConfirmationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity


class TransferConfirmationActivity :
    BaseTransactionActivity<ActivityTransferConfirmationBinding>(ActivityTransferConfirmationBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        
    }

}