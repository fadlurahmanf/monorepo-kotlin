package com.fadlurahmanf.bebas_transaction.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.state.InquiryBankState
import com.fadlurahmanf.bebas_transaction.data.state.PinFavoriteState
import com.fadlurahmanf.bebas_transaction.domain.repositories.FavoriteRepositoryImpl
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val favoriteRepositoryImpl: FavoriteRepositoryImpl,
    private val transactionRepositoryImpl: TransactionRepositoryImpl,
) : BaseViewModel() {

    private val _latestState = MutableLiveData<NetworkState<List<LatestTransactionModel>>>()
    val latestState: LiveData<NetworkState<List<LatestTransactionModel>>> = _latestState

    fun getTransferLatest(favoriteFlow: FavoriteFlow) {
        _latestState.value = NetworkState.LOADING
        val observable: Observable<List<LatestTransactionModel>> = when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                favoriteRepositoryImpl.getLatestTransactionTransfer()
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                favoriteRepositoryImpl.getLatestTransactionPLNPrePaid()
            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                favoriteRepositoryImpl.getLatestTransactionPLNPrePaid()
            }
        }

        baseDisposable.add(observable
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _latestState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _latestState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _favoriteState = MutableLiveData<NetworkState<List<FavoriteContactModel>>>()
    val favoriteState: LiveData<NetworkState<List<FavoriteContactModel>>> = _favoriteState

    fun getTransferFavorite(favoriteFlow: FavoriteFlow) {
        _favoriteState.value = NetworkState.LOADING
        val observable: Observable<List<FavoriteContactModel>> = when (favoriteFlow) {
            FavoriteFlow.TRANSACTION_MENU_TRANSFER -> {
                favoriteRepositoryImpl.getFavoriteTransfer()
            }

            FavoriteFlow.TRANSACTION_MENU_PLN_PREPAID -> {
                favoriteRepositoryImpl.getFavoritePLNPrePaid()
            }

            FavoriteFlow.TRANSACTION_MENU_PULSA_DATA -> {
                favoriteRepositoryImpl.getFavoritePLNPrePaid()
            }
        }

        baseDisposable.add(observable.subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _favoriteState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _favoriteState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _pinState = MutableLiveData<PinFavoriteState>()
    val pinState: LiveData<PinFavoriteState> = _pinState
    fun pinFavorite(id: String, isPinned: Boolean) {
        _pinState.value = PinFavoriteState.LOADING
        baseDisposable.add(favoriteRepositoryImpl.pinFavorite(id, isPinned)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       if (it) {
                                           _pinState.value =
                                               PinFavoriteState.SuccessPinned(isPinned)
                                       } else {
                                           _pinState.value =
                                               PinFavoriteState.FAILED(
                                                   BebasException.generalRC("FALSE_PINNED"),
                                                   favoriteId = id,
                                                   previousStatePinned = isPinned
                                               )
                                       }
                                   },
                                   {
                                       _pinState.value =
                                           PinFavoriteState.FAILED(
                                               BebasException.fromThrowable(it),
                                               favoriteId = id,
                                               previousStatePinned = isPinned
                                           )
                                   },
                                   {}
                               ))
    }

    private val _inquiryBankMasState = MutableLiveData<InquiryBankState>()
    val inquiryBankMasState: LiveData<InquiryBankState> = _inquiryBankMasState

    fun inquiryBankMas(
        destinationAccountNumber: String,
        isFromFavorite: Boolean = false,
        favoriteModel: FavoriteContactModel? = null,
        isFromLatest: Boolean = false,
        latestModel: LatestTransactionModel? = null,
    ) {
        _inquiryBankMasState.value = InquiryBankState.LOADING
        baseDisposable.add(transactionRepositoryImpl.inquiryBankMas(destinationAccountNumber)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _inquiryBankMasState.value = InquiryBankState.SuccessFromFavoriteActivity(
                                           result = it,
                                           isFromFavorite = isFromFavorite,
                                           favoriteModel = favoriteModel,
                                           isFromLatest = isFromLatest,
                                           latestModel = latestModel,
                                           isInquiryBankMas = true
                                       )
                                   },
                                   {
                                       _inquiryBankMasState.value =
                                           InquiryBankState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}