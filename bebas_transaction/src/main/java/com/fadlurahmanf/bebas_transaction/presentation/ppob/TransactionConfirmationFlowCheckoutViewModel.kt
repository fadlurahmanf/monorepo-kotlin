package com.fadlurahmanf.bebas_transaction.presentation.ppob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.payment_service.PaymentSourceConfigResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.transaction.PaymentSourceConfigModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TransactionConfirmationFlowCheckoutViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _paymentSourceState = MutableLiveData<NetworkState<PaymentSourceConfigModel>>()
    val paymentSourceState: LiveData<NetworkState<PaymentSourceConfigModel>> =
        _paymentSourceState

    fun getPaymentSourceConfig(paymentTypeCode: String, amount: Double) {
        _paymentSourceState.value = NetworkState.LOADING
        baseDisposable.add(
            transactionRepositoryImpl.getPaymentSourceConfigReturnModel(
                paymentTypeCode = paymentTypeCode,
                amount = amount
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _paymentSourceState.value = NetworkState.SUCCESS(it)
                    },
                    {
                        _paymentSourceState.value =
                            NetworkState.FAILED(BebasException.fromThrowable(it))
                    },
                    {}
                )
        )
    }
}