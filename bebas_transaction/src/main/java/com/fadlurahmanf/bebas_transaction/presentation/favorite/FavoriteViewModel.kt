package com.fadlurahmanf.bebas_transaction.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.domain.repositories.FavoriteRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val favoriteRepositoryImpl: FavoriteRepositoryImpl
) : BaseViewModel() {

    private val _favoriteState = MutableLiveData<NetworkState<List<FavoriteContactModel>>>()
    val favoriteState: LiveData<NetworkState<List<FavoriteContactModel>>> = _favoriteState

    fun getTransferFavorite() {
        _favoriteState.value = NetworkState.LOADING
        baseDisposable.add(favoriteRepositoryImpl.getFavoriteTransfer()
                               .subscribeOn(Schedulers.io())
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
}