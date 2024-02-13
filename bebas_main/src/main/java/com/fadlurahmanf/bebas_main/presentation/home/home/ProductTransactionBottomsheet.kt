package com.fadlurahmanf.bebas_main.presentation.home.home

import android.content.Intent
import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.argument.ProductTransactionMenuArgument
import com.fadlurahmanf.bebas_main.data.dto.model.home.SubProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.databinding.BottomsheetProductTransactionBinding
import com.fadlurahmanf.bebas_main.presentation.home.adapter.ProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_main.presentation.home.adapter.SubProductTransactionMenuAdapter
import com.fadlurahmanf.bebas_shared.data.argument.transaction.FavoriteArgumentConstant
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow
import com.fadlurahmanf.bebas_ui.bottomsheet.BaseBottomsheet

class ProductTransactionBottomsheet :
    BaseBottomsheet<BottomsheetProductTransactionBinding>(BottomsheetProductTransactionBinding::inflate),
    ProductTransactionMenuAdapter.Callback, SubProductTransactionMenuAdapter.Callback {

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
        binding.tvSelectedMenuLabel.text =
            getString(selectedProductTransactionMenu?.productMenuLabel ?: -1)

        productTransactionMenuAdapter = ProductTransactionMenuAdapter(
            type = ProductTransactionMenuAdapter.Type.BOTTOMSHEET_TRANSACTION_MENU_LABEL
        )
        productTransactionMenuAdapter.setCallback(this)
        productTransactionMenuAdapter.setList(productTransactionMenu)
        binding.rvProductMenu.adapter = productTransactionMenuAdapter

        generateSubProductTransactionMenuBasedOnSelectedProductTransactionMenu()

        val gridLm = GridLayoutManager(requireContext(), 4)
        subProductTransactionMenuAdapter = SubProductTransactionMenuAdapter()
        subProductTransactionMenuAdapter.setCallback(this)
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
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "PULSA_DATA",
                subProductMenuLabel = R.string.credit_and_package,
                subProductImageMenu = R.drawable.ic_subproduct_pulsadata,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "PURCHASE"
                } ?: othersMenu
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "FUND_TRANSFER",
                subProductMenuLabel = R.string.fund_transfer,
                subProductImageMenu = R.drawable.ic_subproduct_transferbank,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "TRANSFER"
                } ?: othersMenu
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "TELKOM_INDIHOME",
                subProductMenuLabel = R.string.telkom_or_indihome,
                subProductImageMenu = R.drawable.ic_subproduct_telkomindihome,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "PAYMENT"
                } ?: othersMenu
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "POSTPAID_PLN",
                subProductMenuLabel = R.string.electricity_bill,
                subProductImageMenu = R.drawable.ic_subproduct_tagihanlistrik,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "PAYMENT"
                } ?: othersMenu
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "TV_CABLE",
                subProductMenuLabel = R.string.tv_cable,
                subProductImageMenu = R.drawable.ic_subproduct_tvkabel,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "PAYMENT"
                } ?: othersMenu
            ),
            SubProductTransactionMenuModel(
                subProductMenuId = "TOPUP_EWALLET",
                subProductMenuLabel = R.string.ewallet,
                subProductImageMenu = R.drawable.ic_subproduct_topupewallet,
                productTransactionMenu = productTransactionMenu.firstOrNull {
                    it.productMenuId == "TOPUP"
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

    override fun onTransactionMenuClicked(menuModel: ProductTransactionMenuModel) {
        if (menuModel.productMenuId == selectedProductTransactionMenu?.productMenuId) {
            return
        }
        selectedProductTransactionMenu = menuModel

        binding.tvSelectedMenuLabel.text =
            getString(selectedProductTransactionMenu?.productMenuLabel ?: -1)

        // get selected data and adapter to change is selected to false
        var selectedIndex = -1
        for (i in 0 until productTransactionMenu.size) {
            if (productTransactionMenu[i].isSelected) {
                productTransactionMenu[i].isSelected = false
                selectedIndex = i
                break
            }
        }
        if (selectedIndex >= 0) {
            productTransactionMenuAdapter.changeData(
                productTransactionMenu[selectedIndex],
                selectedIndex
            )
        }

        // get selected data and adapter to change is selected to true
        selectedIndex = -1
        for (i in 0 until productTransactionMenu.size) {
            if (productTransactionMenu[i].productMenuId == selectedProductTransactionMenu?.productMenuId) {
                productTransactionMenu[i].isSelected = true
                selectedIndex = i
                break
            }
        }

        if (selectedIndex >= 0) {
            productTransactionMenuAdapter.changeData(
                productTransactionMenu[selectedIndex],
                selectedIndex
            )
        }

        subProductTransactionMenu.clear()
        subProductTransactionMenuAdapter.removeAll()
        generateSubProductTransactionMenuBasedOnSelectedProductTransactionMenu()
        subProductTransactionMenuAdapter.setList(subProductTransactionMenu)
    }

    override fun onTransactionMenuClicked(menuModel: SubProductTransactionMenuModel) {
        when (menuModel.subProductMenuId) {
            "FUND_TRANSFER" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_TRANSFER.name
                )
                startActivity(intent)
            }

            "PREPAID_PLN" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID.name
                )
                startActivity(intent)
            }

            "PULSA_DATA" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_PULSA_DATA.name
                )
                startActivity(intent)
            }

            "POSTPAID_PLN" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_PLN_POSTPAID_CHECKOUT.name
                )
                startActivity(intent)
            }

            "TELKOM_INDIHOME" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_TELKOM_INDIHOME.name
                )
                startActivity(intent)
            }

            "TOPUP_EWALLET" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_TOPUP_EWALLET.name
                )
                startActivity(intent)
            }

            "TV_CABLE" -> {
                val intent = Intent(
                    requireContext(),
                    Class.forName("com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity")
                )
                intent.putExtra(
                    FavoriteArgumentConstant.FAVORITE_FLOW,
                    FavoriteFlow.TRANSACTION_MENU_TV_CABLE.name
                )
                startActivity(intent)
            }
        }
    }
}