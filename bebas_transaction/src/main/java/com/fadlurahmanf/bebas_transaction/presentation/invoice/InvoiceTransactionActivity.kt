package com.fadlurahmanf.bebas_transaction.presentation.invoice

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.argument.InvoiceTransactionArgument
import com.fadlurahmanf.bebas_transaction.data.flow.InvoiceTransactionFlow
import com.fadlurahmanf.bebas_transaction.databinding.ActivityInvoiceTransactionBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity

class InvoiceTransactionActivity :
    BaseTransactionActivity<ActivityInvoiceTransactionBinding>(ActivityInvoiceTransactionBinding::inflate) {
    companion object {
        const val FLOW = "FLOW"
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var flow: InvoiceTransactionFlow
    lateinit var argument: InvoiceTransactionArgument

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        val p0Flow = intent.getStringExtra(FLOW)

        if (p0Flow == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_FLOW"))
            return
        }

        flow = enumValueOf(p0Flow)

        val p0Arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ARGUMENT, InvoiceTransactionArgument::class.java)
        } else {
            intent.getParcelableExtra(ARGUMENT)
        }

        if (p0Arg == null) {
            showForcedHomeBottomsheet(BebasException.generalRC("UNKNOWN_ARG"))
            return
        }

        argument = p0Arg



    }

}