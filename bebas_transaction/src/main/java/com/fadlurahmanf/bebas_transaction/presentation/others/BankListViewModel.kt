package com.fadlurahmanf.bebas_transaction.presentation.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.transfer.BankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BankListViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _bankListState = MutableLiveData<NetworkState<List<BankResponse>>>()
    val bankListState: LiveData<NetworkState<List<BankResponse>>> = _bankListState

    fun getBankList() {
        _bankListState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getBankList()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _bankListState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _bankListState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _inquiryBankState = MutableLiveData<NetworkState<InquiryBankResponse>>()
    val inquiryBankState: LiveData<NetworkState<InquiryBankResponse>> = _inquiryBankState

    fun inquiryBankMas(destinationAccountNumber: String) {
        _inquiryBankState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryBankMas(destinationAccountNumber)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryBankState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _inquiryBankState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    fun inquiryOtherBank(sknId: String, destinationAccountNumber: String) {
        _inquiryBankState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryOtherBank(
            sknId,
            destinationAccountNumber
        )
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryBankState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _inquiryBankState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}