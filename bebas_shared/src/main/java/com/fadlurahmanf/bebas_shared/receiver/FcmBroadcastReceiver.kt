package com.fadlurahmanf.bebas_shared.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.fadlurahmanf.bebas_shared.data.argument.transaction.InvoiceTransactionArgumentConstant
import com.fadlurahmanf.bebas_shared.data.dto.NotificationRefreshPulsaTransactionModel
import com.fadlurahmanf.bebas_shared.data.flow.transaction.InvoiceTransactionFlow

open class FcmBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val FCM_LISTENER_ACTION = "com.fadlurahmanf.bebas_fcm.ACTION_FCM_LISTENER"

        fun refreshTransactionPulsa(
            context: Context,
            model: NotificationRefreshPulsaTransactionModel
        ) {
            val intent = Intent("")
            intent.apply {
                action = FCM_LISTENER_ACTION
                putExtra(
                    InvoiceTransactionArgumentConstant.REFRESH_FLOW,
                    InvoiceTransactionFlow.PULSA_PREPAID.name
                )
                putExtra(InvoiceTransactionArgumentConstant.REFRESH_ARGUMENT, model)
            }
            context.sendBroadcast(intent)
            Log.d("BebasLogger", "SEND DATA FROM BROADCAST RECEIVER")
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {}
}