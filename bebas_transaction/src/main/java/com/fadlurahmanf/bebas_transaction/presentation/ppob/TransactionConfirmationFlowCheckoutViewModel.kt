package com.fadlurahmanf.bebas_transaction.presentation.ppob

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.order_service.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.exception.OrderException
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transaction.OrderFeeDetailModel
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

    private val _selectedPaymentSource = MutableLiveData<PaymentSourceModel>()
    val selectedPaymentSource: LiveData<PaymentSourceModel> = _selectedPaymentSource

    private val _loyaltyPaymentSource = MutableLiveData<PaymentSourceModel?>()
    val loyaltyPaymentSource: LiveData<PaymentSourceModel?> = _loyaltyPaymentSource

    fun getPaymentSourceConfig(
        paymentTypeCode: String,
        productCode: String,
        customerId: String,
        customerName: String,
        amount: Double
    ) {
        _oderFeeDetailState.value = NetworkState.IDLE
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
                        _selectedPaymentSource.value = it.mainPaymentSource
                        _loyaltyPaymentSource.value = it.loyaltyPointPaymentSource
                        orderPaymentSchema(
                            productCode = productCode,
                            paymentTypeCode = paymentTypeCode,
                            customerId = customerId,
                            customerName = customerName,
                            useLoyaltyBebasPoin = false
                        )
                    },
                    {
                        _paymentSourceState.value =
                            NetworkState.FAILED(BebasException.fromThrowable(it))
                    },
                    {}
                )
        )
    }

    private val _oderFeeDetailState = MutableLiveData<NetworkState<OrderFeeDetailModel>>()
    val oderFeeDetailState: LiveData<NetworkState<OrderFeeDetailModel>> = _oderFeeDetailState

    fun orderPaymentSchema(
        productCode: String,
        paymentTypeCode: String,
        customerId: String,
        customerName: String,
        useLoyaltyBebasPoin: Boolean = false
    ) {
        _oderFeeDetailState.value = NetworkState.LOADING
        val schemas = arrayListOf<OrderPaymentSchemaRequest.PaymentSourceSchemaRequest>()
        schemas.add(
            OrderPaymentSchemaRequest.PaymentSourceSchemaRequest(
                code = selectedPaymentSource.value?.paymentSource?.code ?: "-",
                accountNumber = selectedPaymentSource.value?.paymentSource?.accountNumber,
                status = true
            )
        )
        if (loyaltyPaymentSource.value != null) {
            schemas.add(
                OrderPaymentSchemaRequest.PaymentSourceSchemaRequest(
                    code = loyaltyPaymentSource.value?.paymentSource?.code ?: "-",
                    accountNumber = selectedPaymentSource.value?.paymentSource?.accountNumber,
                    status = useLoyaltyBebasPoin
                )
            )
        }
        baseDisposable.add(transactionRepositoryImpl.orderPaymentSchemaReturnModel(
            productCode = productCode,
            paymentTypeCode = paymentTypeCode,
            customerId = customerId,
            customerName = customerName,
            sourceGroupId = selectedPaymentSource.value?.paymentSourceConfig?.id
                ?: "-",
            schemas = schemas
        ).subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _oderFeeDetailState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _oderFeeDetailState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}