package com.fadlurahmanf.bebas_main.presentation.home.home

import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.SubProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.BottomsheetProductTransactionBinding
import com.fadlurahmanf.bebas_main.presentation.home.adapter.ProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.SubProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class ProductTransactionBottomsheet :
    BaseBottomsheet<BottomsheetProductTransactionBinding>(BottomsheetProductTransactionBinding::inflate) {

    companion object {
        const val ARGUMENT = "ARGUMENT"
    }

    lateinit var bottomsheetArgument: ProductTransactionMenuArgument

    private var subProductTransactionMenu: ArrayList<SubProductTransactionMenuModel> = arrayListOf()
    private var productTransactionMenu: ArrayList<ProductTransactionMenuModel> = arrayListOf()
    private var selectedProductTransactionMenu: ProductTransactionMenuModel? = null

    private lateinit var productTransactionMenuAdapter: ProductTransactionMenuAdapter
    private lateinit var subProductTransactionMenuAdapter: SubProductTransactionMenuAdapter

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
        productTransactionMenu = ArrayList(bottomsheetArgument.transactionMenus.map {

            val isSelected = it.productMenuId == bottomsheetArgument.selectedProductMenuId
            if (isSelected) {
                selectedProductTransactionMenu = it
            }

            it.copy(
                isSelected = it.productMenuId == bottomsheetArgument.selectedProductMenuId
            )
        })

        productTransactionMenuAdapter = ProductTransactionMenuAdapter(
            type = ProductTransactionMenuAdapter.Type.BOTTOMSHEET_TRANSACTION_MENU_LABEL
        )
        productTransactionMenuAdapter.setList(productTransactionMenu)
        binding.rvProductMenu.adapter = productTransactionMenuAdapter

        generateSubProductTransactionMenuBasedOnSelectedProductTransactionMenu()

        val gridLm = GridLayoutManager(requireContext(), 4)
        subProductTransactionMenuAdapter = SubProductTransactionMenuAdapter()
        subProductTransactionMenuAdapter.setList(subProductTransactionMenu)
        binding.rvSubProduct.adapter = subProductTransactionMenuAdapter
        binding.rvSubProduct.layoutManager = gridLm
    }

    private fun generateSubProductTransactionMenuBasedOnSelectedProductTransactionMenu() {
        val menus = arrayListOf(
            SubProductTransactionMenuModel(
                subProductMenuId = "PREPAID_PLN",
                subProductMenuLabel = R.string.electricity_denom,
                subProductImageMenu = R.drawable.ic_subproduct_tokenlistrik,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "PURCHASE"
                } ?: othersMenu
            )
        )

        subProductTransactionMenu.clear()
        subProductTransactionMenu.addAll(ArrayList(menus.filter {
            it.productTransactionMenu.productMenuId == selectedProductTransactionMenu?.productMenuId
        }.toList()))
    }

    private val othersMenu = ProductTransactionMenuModel(
        productMenuId = "OTHER",
        productMenuLabel = R.string.others,
        productImageMenu = R.drawable.ic_menu_other
    )
}