package com.fadlurahmanf.bebas_transaction.presentation.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.ppob.RefreshStatusResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class InvoiceTransactionViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _refreshState = MutableLiveData<NetworkState<RefreshStatusResponse>>()
    val refreshState: LiveData<NetworkState<RefreshStatusResponse>> = _refreshState

    fun refreshStatusTransaction(transactionId: String) {
        _refreshState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.refreshStatusTransaction(transactionId)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _refreshState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _refreshState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}