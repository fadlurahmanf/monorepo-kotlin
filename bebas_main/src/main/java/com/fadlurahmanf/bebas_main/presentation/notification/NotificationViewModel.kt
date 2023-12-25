package com.fadlurahmanf.bebas_main.presentation.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.fadlurahmanf.bebas_main.data.dto.model.notification.NotificationModel
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import com.fadlurahmanf.bebas_main.presentation.notification.paging_source.NotificationTransactionPagingSource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl,
    private val transactionPagingSource: NotificationTransactionPagingSource,
) : BaseViewModel() {

    private val _notificationState = MutableLiveData<PagingData<NotificationModel>>()
    val notificationState: LiveData<PagingData<NotificationModel>> = _notificationState

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

    private fun getFlowableNotificationPagingData(): Flowable<PagingData<NotificationModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            pagingSourceFactory = {
                transactionPagingSource
            }
        ).flowable
    }

    fun getTransactionDetail(transactionId:String, transactionType:String) {
        baseDisposable.add(mainRepositoryImpl.getDetailTransaction(transactionId, transactionType)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {

                                   },
                                   {},
                                   {}
                               ))
    }
}