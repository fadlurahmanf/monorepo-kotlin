package com.fadlurahmanf.bebas_main.presentation.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.loyalty.CifBebasPoinResponse
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_main.data.dto.model.home.HomeBankAccountModel
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HomeFragmentViewModel @Inject constructor(
    private val mainRepositoryImpl: MainRepositoryImpl
) : BaseViewModel() {

    private val _cifBebasPoinState = MutableLiveData<NetworkState<CifBebasPoinResponse>>()
    val cifBebasPoinState: LiveData<NetworkState<CifBebasPoinResponse>> = _cifBebasPoinState

    fun getBebasPoin() {
        _cifBebasPoinState.value = NetworkState.LOADING
        baseDisposable.add(mainRepositoryImpl.getCifBebasPoin()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _cifBebasPoinState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _cifBebasPoinState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _menuState = MutableLiveData<NetworkState<List<ProductTransactionMenuModel>>>()
    val menuState: LiveData<NetworkState<List<ProductTransactionMenuModel>>> = _menuState

    val menus = arrayListOf<ProductTransactionMenuModel>()

    fun getMenus() {
        baseDisposable.add(mainRepositoryImpl.getTransactionMenu().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       menus.clear()
                                       menus.addAll(it)
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


    private val _bankAccounts = MutableLiveData<NetworkState<List<HomeBankAccountModel>>>()
    val bankAccounts: LiveData<NetworkState<List<HomeBankAccountModel>>> = _bankAccounts

    fun getBankAccounts() {
        _bankAccounts.value = NetworkState.LOADING
        baseDisposable.add(mainRepositoryImpl.getHomeBankAccounts().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _bankAccounts.value = NetworkState.SUCCESS(
                                           it
                                       )
                                   },
                                   {
                                       _bankAccounts.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

    private val _promosState = MutableLiveData<NetworkState<List<ItemPromoResponse>>>()
    val promosState: LiveData<NetworkState<List<ItemPromoResponse>>> = _promosState

    fun getPromos() {
        _promosState.value = NetworkState.LOADING
        baseDisposable.add(mainRepositoryImpl.getHomePagePromo().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _promosState.value = NetworkState.SUCCESS(
                                           it
                                       )
                                   },
                                   {
                                       _promosState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }
}