package com.fadlurahmanf.bebas_transaction.presentation.pin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.pin.PinAttemptResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationRequestModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.PostingPinVerificationResultModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PinVerificationViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _postingTransactionState =
        MutableLiveData<NetworkState<PostingPinVerificationResultModel>>()
    val postingTransactionState: LiveData<NetworkState<PostingPinVerificationResultModel>> =
        _postingTransactionState

    fun postingPinVerification(
        plainPin: String,
        request: PostingPinVerificationRequestModel
    ) {
        _postingTransactionState.value = NetworkState.LOADING
        val disposable = when (request) {
            is PostingPinVerificationRequestModel.FundTranfeerBankMas -> {
                transactionRepositoryImpl.postingTransferBankMas(
                    plainPin,
                    request.fundTransferBankMASRequest
                )
            }

            is PostingPinVerificationRequestModel.PostingPulsaPrePaid -> {
                transactionRepositoryImpl.postingPulsaPrePaid(
                    plainPin,
                    request.postingPulsaPrePaidRequest
                )
            }

            is PostingPinVerificationRequestModel.PostingTelkomIndihome -> {
                transactionRepositoryImpl.postingTelkomIndihome(
                    plainPin,
                    request.postingTelkomIndihomeRequest
                )
            }

            is PostingPinVerificationRequestModel.PostingPLNPrePaidCheckout -> {
                transactionRepositoryImpl.postingTransactionPLNPrePaidCheckout(
                    plainPin,
                    request.postingData
                )
            }
        }
        baseDisposable.add(
            disposable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _postingTransactionState.value = NetworkState.SUCCESS(it)
                    },
                    {
                        _postingTransactionState.value =
                            NetworkState.FAILED(BebasException.fromThrowable(it))
                    },
                    {}
                )
        )
    }

    private val _totalPinAttemptState = MutableLiveData<NetworkState<PinAttemptResponse>>()
    val totalPinAttemptState: LiveData<NetworkState<PinAttemptResponse>> = _totalPinAttemptState
    fun getTotalPinAttempt() {
        _totalPinAttemptState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getTotalPinAttempt()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _totalPinAttemptState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _totalPinAttemptState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}