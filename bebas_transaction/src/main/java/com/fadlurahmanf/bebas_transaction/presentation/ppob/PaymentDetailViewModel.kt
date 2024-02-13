package com.fadlurahmanf.bebas_transaction.presentation.ppob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryResultModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PaymentDetailViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _selectedPaymentSourceState = MutableLiveData<NetworkState<PaymentSourceModel>>()
    val selectedPaymentSourceState: LiveData<NetworkState<PaymentSourceModel>> =
        _selectedPaymentSourceState

    var selectedPaymentSource: PaymentSourceModel? = null

    fun selectPaymentSource(paymentSource: PaymentSourceModel) {
        selectedPaymentSource = paymentSource
        _selectedPaymentSourceState.value = NetworkState.SUCCESS(paymentSource)
    }

    private val _selectedDenomModel = MutableLiveData<PPOBDenomModel?>(null)
    val selectedDenomModel: LiveData<PPOBDenomModel?> = _selectedDenomModel

    fun selectDenomModel(denom: PPOBDenomModel) {
        _selectedDenomModel.value = denom
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

    private val _plnPrePaidDenomState = MutableLiveData<NetworkState<List<PPOBDenomModel>>>()
    val plnPrePaidDenomState: LiveData<NetworkState<List<PPOBDenomModel>>> =
        _plnPrePaidDenomState

    fun getPLNPrePaidDenom() {
        _plnPrePaidDenomState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getPLNPrePaidDenomModel()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       if (it.isNotEmpty()) {
                                           _plnPrePaidDenomState.value =
                                               NetworkState.SUCCESS(it)
                                       } else {
                                           _plnPrePaidDenomState.value = NetworkState.FAILED(
                                               BebasException.generalRC("PLN_PREPAID_00")
                                           )
                                       }
                                   },
                                   {
                                       _plnPrePaidDenomState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _inquiryCheckoutState = MutableLiveData<NetworkState<InquiryResultModel>>()
    val inquiryCheckoutState: LiveData<NetworkState<InquiryResultModel>> =
        _inquiryCheckoutState

    fun inquiryPLNPrePaid(customerId: String, productCode: String) {
        _inquiryCheckoutState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryPLNPrePaidCheckoutReturnModel(
            customerId,
            productCode
        )
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryCheckoutState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _inquiryCheckoutState.value = NetworkState.FAILED(
                                           BebasException.fromThrowable(it)
                                       )
                                   },
                                   {}
                               ))
    }
}