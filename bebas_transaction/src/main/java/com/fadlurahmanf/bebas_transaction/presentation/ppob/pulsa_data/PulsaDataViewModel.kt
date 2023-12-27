package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.ppob.PulsaDenomResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.ppob.PulsaDenomModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class PulsaDataViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

    private val _pulsaDenomState = MutableLiveData<NetworkState<List<PulsaDenomModel>>>()
    val pulsaDenomState: LiveData<NetworkState<List<PulsaDenomModel>>> = _pulsaDenomState

    fun getPulsaDenom(provider: String, providerImage: String? = null) {
        _pulsaDenomState.value = NetworkState.LOADING
        baseDisposable.add(transactionRepositoryImpl.getPulsaDenomModel(provider, providerImage)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _pulsaDenomState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _pulsaDenomState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}