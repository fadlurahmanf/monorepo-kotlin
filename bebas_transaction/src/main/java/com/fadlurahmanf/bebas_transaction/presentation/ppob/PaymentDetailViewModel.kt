package com.fadlurahmanf.bebas_transaction.presentation.ppob

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PPOBDenomModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PaymentDetailViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _selectedBankAccountState = MutableLiveData<NetworkState<BankAccountResponse>>()
    val selectedBankAccountState: LiveData<NetworkState<BankAccountResponse>> =
        _selectedBankAccountState

    var selectedBankAccount: BankAccountResponse? = null
    private val bankAccounts: ArrayList<BankAccountResponse> = arrayListOf()

    fun getBankAccounts() {
        _selectedBankAccountState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getBankAccounts()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       if (it.isNotEmpty()) {
                                           _selectedBankAccountState.value =
                                               NetworkState.SUCCESS(it.first())
                                           selectedBankAccount = it.first()
                                           bankAccounts.clear()
                                           bankAccounts.addAll(it)
                                       } else {
                                           _selectedBankAccountState.value = NetworkState.FAILED(
                                               BebasException.generalRC("AC_01")
                                           )
                                       }
                                   },
                                   {
                                       _selectedBankAccountState.value =
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
}