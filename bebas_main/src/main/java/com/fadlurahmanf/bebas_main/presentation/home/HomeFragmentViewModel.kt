package com.fadlurahmanf.bebas_main.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.data.dto.menu.TransactionMenuModel
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl
) : BaseViewModel() {

    private val _menuState = MutableLiveData<NetworkState<List<TransactionMenuModel>>>()
    val menuState: LiveData<NetworkState<List<TransactionMenuModel>>> = _menuState

    fun getMenus() {
        baseDisposable.add(mainRepositoryImpl.getTransactionMenu().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _menuState.value = NetworkState.SUCCESS(
                                           it
                                       )
                                   },
                                   {
                                       _menuState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}