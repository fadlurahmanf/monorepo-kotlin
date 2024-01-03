package com.fadlurahmanf.bebas_loyalty.presentation.history

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.fadlurahmanf.bebas_loyalty.data.dto.HistoryLoyaltyModel
import com.fadlurahmanf.bebas_loyalty.presentation.history.paging_source.LoyaltyHistoryPagingSource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HistoryLoyaltyViewModel @Inject constructor(
    private val loyaltyHistoryPagingSource: LoyaltyHistoryPagingSource
) : BaseViewModel() {

    private val _notificationState = MutableLiveData<PagingData<HistoryLoyaltyModel>>()
    val notificationState: LiveData<PagingData<HistoryLoyaltyModel>> = _notificationState

    fun getNotification(context: Context) {
        baseDisposable.add(getFlowableNotificationPagingData()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _notificationState.value = it
                                   },
                                   {
                                   },
                                   {}
                               ))
    }

    private fun getFlowableNotificationPagingData(): Flowable<PagingData<HistoryLoyaltyModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                loyaltyHistoryPagingSource
            }
        ).flowable
    }
}