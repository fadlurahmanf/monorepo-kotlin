package com.fadlurahmanf.bebas_transaction.presentation.others

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.state.BankListState
import com.fadlurahmanf.bebas_transaction.data.state.InquiryState
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BankListViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _bankListState = MutableLiveData<BankListState>()
    val bankListState: LiveData<BankListState> = _bankListState

    fun getBankList() {
        _bankListState.value = BankListState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getBankList()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       val banks: ArrayList<ItemBankResponse> = ArrayList(it)
                                       Log.d("BebasLogger", "BANKS: ${banks.size}")
                                       val otherBanks =
                                           transactionRepositoryImpl.removeTopBanks(banks)
                                       Log.d("BebasLogger", "OTHER BANKS: ${otherBanks.size}")
                                       val topBanks = transactionRepositoryImpl.getTopBanks(banks)
                                       Log.d("BebasLogger", "TOP BANKS: ${topBanks.size}")
                                       _bankListState.value = BankListState.SUCCESS(
                                           otherBanks, topBanks
                                       )
                                   },
                                   {
                                       _bankListState.value =
                                           BankListState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _inquiryState = MutableLiveData<InquiryState>()
    val inquiryState: LiveData<InquiryState> = _inquiryState

    fun inquiryBankMas(destinationAccountNumber: String, bank: ItemBankResponse) {
        _inquiryState.value = InquiryState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryBankMas(destinationAccountNumber)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryState.value =
                                           InquiryState.SuccessFromListActivity(
                                               result = it,
                                               destinationAccount = destinationAccountNumber,
                                               selectedBank = bank,
                                               isInquiryBankMas = true
                                           )
                                   },
                                   {
                                       _inquiryState.value =
                                           InquiryState.FailedBebas(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    fun inquiryOtherBank(sknId: String, destinationAccountNumber: String, bank: ItemBankResponse) {
        _inquiryState.value = InquiryState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryOtherBank(
            sknId,
            destinationAccountNumber
        )
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryState.value =
                                           InquiryState.SuccessFromListActivity(
                                               result = it,
                                               destinationAccount = destinationAccountNumber,
                                               selectedBank = bank,
                                               isInquiryBankMas = false
                                           )
                                   },
                                   {
                                       _inquiryState.value =
                                           InquiryState.FailedBebas(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}