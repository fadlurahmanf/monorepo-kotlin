package com.fadlurahmanf.bebas_main.presentation.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import androidx.paging.map
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.data.dto.model.notification.NotificationModel
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import com.fadlurahmanf.bebas_main.presentation.notification.paging_source.NotificationTransactionPagingSource
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl,
) : BaseViewModel() {

    private val _unreadTransaction = MutableLiveData<Int>(0)
    val unreadTransaction: LiveData<Int> = _unreadTransaction

    fun getTransactionNotifCount() {
        baseDisposable.add(mainRepositoryImpl.getUnreadTransactionNotificationCount()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _unreadTransaction.value = it
                                   },
                                   {},
                                   {}
                               ))
    }

    fun updateUnreadTransaction(totalUnread: Int) {
        _unreadTransaction.value = totalUnread
    }

    private val _notificationState = MutableLiveData<PagingData<NotificationModel>>()
    val notificationState: LiveData<PagingData<NotificationModel>> = _notificationState

    var allFetchedNotification = listOf<NotificationModel>()

    fun getNotification(context: Context) {
        baseDisposable.add(getFlowableNotificationPagingData(context)
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

    private fun getFlowableNotificationPagingData(context: Context): Flowable<PagingData<NotificationModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                NotificationTransactionPagingSource(context, mainRepositoryImpl)
            }
        ).flowable.cachedIn(viewModelScope)
    }

    private val _readNotificationState = MutableLiveData<NetworkState<Boolean>>()
    val readNotificationState: LiveData<NetworkState<Boolean>> = _readNotificationState

    fun updateIsReadNotification(notificationId: String) {
        _readNotificationState.value = NetworkState.LOADING
        baseDisposable.add(mainRepositoryImpl.updateReadNotification(notificationId)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _readNotificationState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _readNotificationState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    fun getTransactionDetail(transactionId: String, transactionType: String) {
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