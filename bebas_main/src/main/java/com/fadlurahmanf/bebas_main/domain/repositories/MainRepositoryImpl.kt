package com.fadlurahmanf.bebas_main.domain.repositories

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.InboxRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.UnreadNotificationCountResponse
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.model.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    context: Context,
    private val cmsRemoteDatasource: CmsRemoteDatasource,
    private val transactionRemoteDatasource: TransactionRemoteDatasource,
    private val inboxRemoteDatasource: InboxRemoteDatasource,
) {

    fun getUnreadNotificationCount(): Observable<UnreadNotificationCountResponse> {
        return inboxRemoteDatasource.getUnreadNotificationCount().map {
            if (it.data == null) {
                throw BebasException.generalRC("UNC_00")
            }
            it.data!!
        }
    }

    fun getUnreadTransactionNotificationCount(): Observable<Int> {
        return getUnreadNotificationCount().map { resp ->
            val details = resp.details ?: listOf()
            details.firstOrNull { detail ->
                detail.type == "TRANSACTION"
            }?.total ?: 0
        }
    }

    fun getTransactionMenu(): Observable<List<ProductTransactionMenuModel>> {
        return cmsRemoteDatasource.getTransactionMenu()
            .map {
                if (it.data == null) {
                    throw BebasException.generalRC("DATA_MISSING")
                }

                val menus = it.data!!
                val menusModel = ArrayList<ProductTransactionMenuModel>()
                menus.forEach { menuResp ->
                    when (menuResp.menuId) {
                        "TRANSFER" -> {
                            menusModel.add(
                                ProductTransactionMenuModel(
                                    productMenuId = menuResp.menuId ?: "-",
                                    productMenuLabel = R.string.transfer,
                                    productImageMenu = R.drawable.ic_menu_transfer
                                )
                            )
                        }

                        "PAYMENT" -> {
                            menusModel.add(
                                ProductTransactionMenuModel(
                                    productMenuId = menuResp.menuId ?: "-",
                                    productMenuLabel = R.string.payment,
                                    productImageMenu = R.drawable.ic_menu_payment
                                )
                            )
                        }

                        "PURCHASE" -> {
                            menusModel.add(
                                ProductTransactionMenuModel(
                                    productMenuId = menuResp.menuId ?: "-",
                                    productMenuLabel = R.string.purchase,
                                    productImageMenu = R.drawable.ic_menu_purchase
                                )
                            )
                        }

                        "TOPUP" -> {
                            menusModel.add(
                                ProductTransactionMenuModel(
                                    productMenuId = menuResp.menuId ?: "-",
                                    productMenuLabel = R.string.topup,
                                    productImageMenu = R.drawable.ic_menu_topup
                                )
                            )
                        }

                        "CARDLESS_WD" -> {
                            menusModel.add(
                                ProductTransactionMenuModel(
                                    productMenuId = menuResp.menuId ?: "-",
                                    productMenuLabel = R.string.cardless_withdrawal,
                                    productImageMenu = R.drawable.ic_menu_transfer
                                )
                            )
                        }
                    }
                }
                menusModel.add(
                    ProductTransactionMenuModel(
                        productMenuId = "OTHERS",
                        productMenuLabel = R.string.others,
                        productImageMenu = R.drawable.ic_menu_other
                    )
                )
                menusModel
            }
    }

    fun getListBankAccount(): Observable<List<BankAccountResponse>> {
        return transactionRemoteDatasource.getBankAccounts().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            if (it.data?.isEmpty() == true) {
                throw BebasException.generalRC("MBA_00")
            }
            val bankAccounts = it.data!!
            bankAccounts
        }
    }

    fun getHomeBankAccounts(): Observable<List<HomeBankAccountModel>> {
        return getListBankAccount().map { list ->
            val newList = list.map {
                HomeBankAccountModel(
                    accountBalance = it.workingBalance ?: -1.0,
                    accountNumber = it.accountNumber ?: "-",
                    accountName = it.accountName ?: "-",
                    response = it,
                )
            }
            newList[0].isPinned = true
            newList
        }
    }

    fun getHomePagePromo(): Observable<List<ItemPromoResponse>> {
        return cmsRemoteDatasource.getHomepagePromo().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }
            it.data!!
        }
    }

    fun getTransactionNotification(page: Int): Single<NotificationResponse> {
        return inboxRemoteDatasource.getTransactionNotification(page = page).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }
            it.data!!
        }
    }

    fun getDetailTransaction(transactionId: String, transactionType: String): Observable<String> {
        return transactionRemoteDatasource.getTransactionDetail(transactionId, transactionType)
            .map {
                ""
            }
    }
}