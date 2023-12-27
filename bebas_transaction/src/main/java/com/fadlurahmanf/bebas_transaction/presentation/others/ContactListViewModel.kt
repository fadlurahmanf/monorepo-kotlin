package com.fadlurahmanf.bebas_transaction.presentation.others

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    private val _contacts = MutableLiveData<NetworkState<List<BebasContactModel>>>()
    val contacts: LiveData<NetworkState<List<BebasContactModel>>> = _contacts

    fun getListContact(context: Context) {
        _contacts.value = NetworkState.LOADING
        baseDisposable.add(deviceRepository.getContactsWithIndicator(context)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(
                                   {
                                       _contacts.value = NetworkState.SUCCESS(it)
                                   },
                                   {
                                       _contacts.value =
                                           NetworkState.FAILED(BebasException.fromThrowable(it))
                                   },
                                   {}
                               ))
    }

}