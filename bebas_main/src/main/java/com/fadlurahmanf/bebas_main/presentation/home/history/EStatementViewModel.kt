package com.fadlurahmanf.bebas_main.presentation.home.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EStatementViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl
) : BaseViewModel() {

    private val _estatementState = MutableLiveData<NetworkState<EStatementResponse>>()
    val estatementState: LiveData<NetworkState<EStatementResponse>> = _estatementState

    fun getEStatements() {
        _estatementState.value = NetworkState.LOADING
        baseDisposable.add(mainRepositoryImpl.getEStatements().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _estatementState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _estatementState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}