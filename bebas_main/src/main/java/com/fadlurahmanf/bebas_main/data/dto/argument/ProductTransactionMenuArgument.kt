package com.fadlurahmanf.bebas_main.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductTransactionMenuArgument(
    val selectedProductMenuId: String,
    val transactionMenus: List<ProductTransactionMenuModel>
) : Parcelable
