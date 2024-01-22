package com.fadlurahmanf.bebas_loyalty.presentation.program

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_loyalty.data.dto.ProgramCategoryModel
import com.fadlurahmanf.bebas_loyalty.domain.repositories.LoyaltyRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ProgramListViewModel @Inject constructor(
    private val loyaltyRepositoryImpl: LoyaltyRepositoryImpl
) : BaseViewModel() {

    private val _programCategoryState =
        MutableLiveData<NetworkState<List<ProgramCategoryModel>>>()
    val programCategoryState: LiveData<NetworkState<List<ProgramCategoryModel>>> =
        _programCategoryState

    fun getProgramListCategory() {
        _programCategoryState.value = NetworkState.LOADING
        baseDisposable.add(loyaltyRepositoryImpl.getProgramCategoryWithAllReturnModel().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _programCategoryState.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _programCategoryState.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

}