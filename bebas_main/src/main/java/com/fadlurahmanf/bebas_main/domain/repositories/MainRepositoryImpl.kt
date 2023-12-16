package com.fadlurahmanf.bebas_main.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.menu.TransactionMenuModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    context: Context,
    private val cmsRemoteDatasource: CmsRemoteDatasource,
) {

    fun getTransactionMenu(): Observable<List<TransactionMenuModel>> {
        return cmsRemoteDatasource.getTransactionMenu()
            .map {
                if (it.data == null) {
                    throw BebasException.generalRC("DATA_MISSING")
                }

                val menus = it.data!!
                val menusModel = ArrayList<TransactionMenuModel>()
                menus.forEach { menuResp ->
                    when (menuResp.menuId) {
                        "TRANSFER" -> {
                            menusModel.add(
                                TransactionMenuModel(
                                    menuId = menuResp.menuId ?: "-",
                                    menuLabel = R.string.transfer,
                                    imageMenu = R.drawable.ic_menu_transfer
                                )
                            )
                        }

                        "PAYMENT" -> {
                            menusModel.add(
                                TransactionMenuModel(
                                    menuId = menuResp.menuId ?: "-",
                                    menuLabel = R.string.payment,
                                    imageMenu = R.drawable.ic_menu_payment
                                )
                            )
                        }

                        "PURCHASE" -> {
                            menusModel.add(
                                TransactionMenuModel(
                                    menuId = menuResp.menuId ?: "-",
                                    menuLabel = R.string.purchase,
                                    imageMenu = R.drawable.ic_menu_purchase
                                )
                            )
                        }

                        "TOPUP" -> {
                            menusModel.add(
                                TransactionMenuModel(
                                    menuId = menuResp.menuId ?: "-",
                                    menuLabel = R.string.topup,
                                    imageMenu = R.drawable.ic_menu_topup
                                )
                            )
                        }

                        "CARDLESS_WD" -> {
                            menusModel.add(
                                TransactionMenuModel(
                                    menuId = menuResp.menuId ?: "-",
                                    menuLabel = R.string.cardless_withdrawal,
                                    imageMenu = R.drawable.ic_menu_transfer
                                )
                            )
                        }
                    }
                }
                menusModel.add(
                    TransactionMenuModel(
                        menuId = "OTHERS",
                        menuLabel = R.string.others,
                        imageMenu = R.drawable.ic_menu_other
                    )
                )
                menusModel
            }
    }
}