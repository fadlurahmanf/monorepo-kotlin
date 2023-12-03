package com.fadlurahmanf.bebas_onboarding.presentation.vc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.VideoCallRepository
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class VideoCallViewModel @Inject constructor(
    private val videoCallRepository: VideoCallRepository
) : BaseViewModel() {

    private val _initConnectionState = MutableLiveData<NetworkState<ConnectionResponse>>()
    val initConnectionState: LiveData<NetworkState<ConnectionResponse>> = _initConnectionState

    fun initializeConnection(sessionId: String) {
        _initConnectionState.value = NetworkState.LOADING
        compositeDisposable().add(videoCallRepository.initializeConnection(sessionId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _initConnectionState.value = NetworkState.SUCCESS(it)
                                          },
                                          {
                                              _initConnectionState.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }
}