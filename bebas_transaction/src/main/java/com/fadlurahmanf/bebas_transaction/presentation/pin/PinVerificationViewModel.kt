package com.fadlurahmanf.bebas_transaction.presentation.pin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PinVerificationViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _fundTransferState = MutableLiveData<NetworkState<FundTransferResponse>>()
    val fundTransferState: LiveData<NetworkState<FundTransferResponse>> = _fundTransferState
    fun fundTransferBankMas(
        plainPin: String,
        fundTransferBankMASRequest: FundTransferBankMASRequest
    ) {
        try {
            _fundTransferState.value = NetworkState.LOADING
            baseDisposable.add(
                transactionRepositoryImpl.postingTransferBankMas(
                    plainPin = plainPin,
                    fundTransferRequest = fundTransferBankMASRequest
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            _fundTransferState.value = NetworkState.SUCCESS(it)
                        },
                        {
                            _fundTransferState.value =
                                NetworkState.FAILED(BebasException.fromThrowable(it))
                        },
                        {}
                    )
            )
        } catch (e: Throwable) {
            _fundTransferState.value = NetworkState.FAILED(BebasException.fromThrowable(e))
        }
    }
}