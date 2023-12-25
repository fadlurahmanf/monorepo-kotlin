package com.fadlurahmanf.bebas_main.presentation.home.home

import android.os.Build
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.SubProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.BottomsheetProductTransactionBinding
import com.fadlurahmanf.bebas_main.presentation.home.adapter.ProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class ProductTransactionBottomsheet :
    BaseBottomsheet<BottomsheetProductTransactionBinding>(BottomsheetProductTransactionBinding::inflate) {

    companion object {
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var bottomsheetArgument: ProductTransactionMenuArgument

    private var productTransaction: ArrayList<SubProductTransactionMenuModel> = arrayListOf()
    private var transactionMenus: ArrayList<ProductTransactionMenuModel> = arrayListOf()

    private lateinit var productTransactionMenuAdapter: ProductTransactionMenuAdapter

    override fun setup() {
        val p0arg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARGUMENT, ProductTransactionMenuArgument::class.java)
        } else {
            arguments?.getParcelable(ARGUMENT)
        }

        if (p0arg == null) {
            return
        }

        bottomsheetArgument = p0arg
        transactionMenus = ArrayList(bottomsheetArgument.transactionMenus.map {
            it.copy(
                isSelected = it.productMenuId == bottomsheetArgument.selectedProductMenuId
            )
        })

        generateProductTransactionMenus()
        productTransactionMenuAdapter = ProductTransactionMenuAdapter(
            type = ProductTransactionMenuAdapter.Type.BOTTOMSHEET_TRANSACTION_MENU_LABEL
        )
        productTransactionMenuAdapter.setList(transactionMenus)
        binding.rvProductMenu.adapter = productTransactionMenuAdapter
    }

    private fun generateProductTransactionMenus() {
        val menus = arrayListOf(
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

    private val othersMenu = ProductTransactionMenuModel(
        productMenuId = "OTHER",
        productMenuLabel = R.string.others,
        productImageMenu = R.drawable.ic_menu_other
    )
}