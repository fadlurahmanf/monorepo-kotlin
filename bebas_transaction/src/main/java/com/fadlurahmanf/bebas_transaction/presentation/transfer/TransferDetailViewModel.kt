package com.fadlurahmanf.bebas_transaction.presentation.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.state.TransferDetailState
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TransferDetailViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    companion object {
        const val MINIMUM_TRANSFER = 10000L
    }

    private val _transferState = MutableLiveData<TransferDetailState>()
    val transferState: LiveData<TransferDetailState> = _transferState

    fun verifyMinimumInputNominal(nominal: Long) {
        _transferState.value = TransferDetailState.IDLE
        if (nominal < MINIMUM_TRANSFER) {
            _transferState.value =
                TransferDetailState.MinimumNominalTransferFailed(MINIMUM_TRANSFER)
        } else {
            _transferState.value = TransferDetailState.SUCCESS(nominal)
        }
    }

    private val _selectedPaymentSourceState = MutableLiveData<NetworkState<PaymentSourceModel>>()
    val selectedPaymentSourceState: LiveData<NetworkState<PaymentSourceModel>> =
        _selectedPaymentSourceState

    var selectedPaymentSource: PaymentSourceModel? = null
    val paymentSources: ArrayList<PaymentSourceModel> = arrayListOf()

    fun selectPaymentSource(paymentSource: PaymentSourceModel) {
        selectedPaymentSource = paymentSource
        _selectedPaymentSourceState.value = NetworkState.SUCCESS(paymentSource)
    }

    fun getPaymentSources() {
        _selectedPaymentSourceState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getPaymentSourceModelFromGetBankAccounts()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       if (it.isNotEmpty()) {
                                           _selectedPaymentSourceState.value =
                                               NetworkState.SUCCESS(it.first())
                                           selectedPaymentSource = it.first()
                                           paymentSources.clear()
                                           paymentSources.addAll(it)
                                       } else {
                                           _selectedPaymentSourceState.value = NetworkState.FAILED(
                                               BebasException.generalRC("AC_01")
                                           )
                                       }
                                   },
                                   {
                                       _selectedPaymentSourceState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}
