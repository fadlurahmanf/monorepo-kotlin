package com.fadlurahmanf.bebas_transaction.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class SelectPaymentSourceViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _paymentSourcesState = MutableLiveData<NetworkState<List<PaymentSourceModel>>>()
    val paymentSourcesState: LiveData<NetworkState<List<PaymentSourceModel>>> =
        _paymentSourcesState

    fun getPaymentSources() {
        _paymentSourcesState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getPaymentSourceModelFromGetBankAccounts()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       if (it.isNotEmpty()) {
                                           _paymentSourcesState.value =
                                               NetworkState.SUCCESS(it)
                                       } else {
                                           _paymentSourcesState.value = NetworkState.FAILED(
                                               BebasException.generalRC("PC_01")
                                           )
                                       }
                                   },
                                   {
                                       _paymentSourcesState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}