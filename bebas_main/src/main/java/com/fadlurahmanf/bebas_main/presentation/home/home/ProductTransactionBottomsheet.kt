package com.fadlurahmanf.bebas_main.presentation.home.home

import android.os.Build
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.SubProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.BottomsheetProductTransactionBinding
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class ProductTransactionBottomsheet :
    BaseBottomsheet<BottomsheetProductTransactionBinding>(BottomsheetProductTransactionBinding::inflate) {

    companion object {
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var bottomsheetArgument: ProductTransactionMenuArgument

    private lateinit var productTransaction: List<SubProductTransactionMenuModel>
    private lateinit var transactionMenus: List<ProductTransactionMenuModel>

    override fun setup() {
        val p0arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARGUMENT, ProductTransactionMenuArgument::class.java)
        } else {
            arguments?.getParcelable(ARGUMENT)
        }

        if (p0arg == null) {
            return
        }

        transactionMenus = bottomsheetArgument.transactionMenus

        bottomsheetArgument = p0arg

        generateProductTransactionMenus()
    }

    private fun generateProductTransactionMenus() {
        val menus = listOf(
            SubProductTransactionMenuModel(
                subProductMenuId = "PREPAID_PLN",
                subProductMenuLabel = R.string.electricity_denom,
                subProductImageMenu = R.drawable.ic_subproduct_tokenlistrik,
                productTransactionMenu = transactionMenus.firstOrNull {
                    it.productMenuId == "PURCHASE"
                } ?: othersMenu
            )
        )
        productTransaction = menus
    }

    val othersMenu = ProductTransactionMenuModel(
        productMenuId = "",
        productMenuLabel = R.string.others,
        productImageMenu = R.drawable.ic_menu_other
    )
}