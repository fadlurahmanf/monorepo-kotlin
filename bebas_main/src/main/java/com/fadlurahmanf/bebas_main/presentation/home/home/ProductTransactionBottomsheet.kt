package com.fadlurahmanf.bebas_main.presentation.home.home

import android.os.Build
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.TransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.BottomsheetProductTransactionBinding
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class ProductTransactionBottomsheet :
    BaseBottomsheet<BottomsheetProductTransactionBinding>(BottomsheetProductTransactionBinding::inflate) {

    companion object {
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var bottomsheetArgument: ProductTransactionMenuArgument

    private lateinit var productTransaction: List<ProductTransactionMenuModel>
    private lateinit var transactionMenus: List<TransactionMenuModel>

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
            ProductTransactionMenuModel(
                productMenuId = "PREPAID_PLN",
                productMenuLabel = R.string.electricity_denom,
                productImageMenu = R.drawable.ic_subproduct_tokenlistrik,
                transactionMenu = transactionMenus.firstOrNull {
                    it.menuId == "PURCHASE"
                } ?: othersMenu
            )
        )
        productTransaction = menus
    }

    val othersMenu = TransactionMenuModel(
        menuId = "",
        menuLabel = R.string.others,
        imageMenu = R.drawable.ic_menu_other
    )
}