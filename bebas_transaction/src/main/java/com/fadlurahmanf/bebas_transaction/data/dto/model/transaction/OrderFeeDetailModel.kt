package com.fadlurahmanf.bebas_transaction.data.dto.model.transaction

import android.os.Parcelable
import androidx.annotation.StyleRes
import com.fadlurahmanf.bebas_api.data.dto.order_service.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.CheckoutTransactionDataRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFeeDetailModel(
    val orderId:String,
    val paymentConfigGroupId:String,
    val schemas: List<CheckoutTransactionDataRequest.Schema>,
    val paymentTypeCode: String,
    val total: Double,
    val details: ArrayList<Detail> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class Detail(
        val label: String,
        val value: Double,
        @StyleRes val valueStyle: Int? = null,
    ) : Parcelable
}
