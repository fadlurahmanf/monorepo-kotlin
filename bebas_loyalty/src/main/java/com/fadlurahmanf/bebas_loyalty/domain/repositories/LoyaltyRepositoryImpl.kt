package com.fadlurahmanf.bebas_loyalty.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.CmsRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.general.BasePaginationTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.ProgramCategoryResponse
import com.fadlurahmanf.bebas_loyalty.data.dto.ProgramCategoryModel
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoyaltyRepositoryImpl @Inject constructor(
    context: Context,
    private val cmsRemoteDatasource: CmsRemoteDatasource,
    private val transactionRemoteDatasource: TransactionRemoteDatasource,
) {

    fun getHistoryLoyalty(
        offset: Int,
        status: String? = null
    ): Single<BasePaginationTransactionResponse<List<HistoryLoyaltyResponse>>> {
        return transactionRemoteDatasource.getAllHistory(offset = offset, status = status).map {
            if (it.data == null) {
                throw BebasException.generalRC("HL_00")
            }
            it.data!!
        }
    }

    fun getProgramCategory(): Observable<List<ProgramCategoryResponse>> {
        return cmsRemoteDatasource.getProgramCategory().map {
            if (it.data == null) {
                throw BebasException.generalRC("PC_00")
            }
            it.data!!
        }
    }

    fun getProgramCategoryWithAllReturnModel(): Observable<List<ProgramCategoryModel>> {
        return getProgramCategory().map { categories ->
            val newList = arrayListOf<ProgramCategoryResponse>()
            newList.add(
                ProgramCategoryResponse(
                    categoryId = "ALL",
                    categoryLabel = if (BebasShared.language == "en-US") "All" else "Semua"
                )
            )
            newList.addAll(categories)
            newList.map { category ->
                ProgramCategoryModel(
                    id = category.categoryId ?: "-",
                    label = category.categoryLabel ?: "-",
                    isSelected = false,
                    item = category
                )
            }
        }
    }


}