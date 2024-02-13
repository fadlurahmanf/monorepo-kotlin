package com.fadlurahmanf.bebas_main.domain.repositories

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.data.datasources.CifRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.InboxRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_api.data.dto.home.HomePageBannerInfoResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.CifBebasPoinResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.UnreadNotificationCountResponse
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.model.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.io.InputStream
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    context: Context,
    private val cifRemoteDatasource: CifRemoteDatasource,
    private val cmsRemoteDatasource: CmsRemoteDatasource,
    private val inboxRemoteDatasource: InboxRemoteDatasource,
    private val transactionRemoteDatasource: TransactionRemoteDatasource,
) {

    fun getCifBebasPoin(): Observable<CifBebasPoinResponse> {
        return cifRemoteDatasource.getCifBebasPoin().map {
            if (it.data == null) {
                throw BebasException.generalRC("BP_00")
            }
            it.data!!
        }
    }

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

    fun getHomePageBannerInfoPromo(): Observable<List<HomePageBannerInfoResponse>> {
        return cmsRemoteDatasource.getHomepageBannerInfo().map {
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

    fun updateReadNotification(notificationId: String): Observable<Boolean> {
        return inboxRemoteDatasource.updateReadNotification(notificationId).map {
            true
        }
    }

    fun getDetailTransaction(transactionId: String, transactionType: String): Observable<String> {
        return transactionRemoteDatasource.getTransactionDetail(transactionId, transactionType)
            .map {
                ""
            }
    }

    fun getEStatements(): Observable<EStatementResponse> {
        return transactionRemoteDatasource.getBankAccounts().flatMap { baseBankAccounts ->
            if (baseBankAccounts.data == null || baseBankAccounts.data?.isEmpty() == true) {
                throw BebasException.generalRC("BA_00")
            }
            val accountNumber = baseBankAccounts.data?.first()?.accountNumber
                ?: throw BebasException.generalRC("AC_00")
            cifRemoteDatasource.getEStatements(accountNumber).map { baseEstatement ->
                if (baseEstatement.data == null) {
                    throw BebasException.generalRC("ESTATEMENT_00")
                }
                baseEstatement.data!!
            }
        }
    }

    fun downloadEStatement(accountNumber: String, year: Int, month: Int): Observable<InputStream> {
        return cifRemoteDatasource.downloadEStatement(accountNumber, year, month).let {
            if (it != null) {
                Observable.just(it)
            } else {
                throw BebasException.generalRC("DES_00")
            }
        }
    }
}