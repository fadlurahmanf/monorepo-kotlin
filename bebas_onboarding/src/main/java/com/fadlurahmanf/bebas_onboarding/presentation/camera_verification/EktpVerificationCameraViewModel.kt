package com.fadlurahmanf.bebas_onboarding.presentation.camera_verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.ocr.OcrResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.data.state.InitEktpCameraResult
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.data.entity.BebasDecryptedEntity
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EktpVerificationCameraViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl,
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    private val _ocrState = MutableLiveData<NetworkState<OcrResponse>>()
    val ocrState: LiveData<NetworkState<OcrResponse>> = _ocrState

    fun getOCRv2(base64Image: String) {
        _ocrState.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.getOCRv2(base64Image)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _ocrState.value = NetworkState.SUCCESS(it)
                                          },
                                          {
                                              _ocrState.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    private val _initState = MutableLiveData<InitEktpCameraResult>()
    val initState: LiveData<InitEktpCameraResult> = _initState

    fun initResult() {
        compositeDisposable().add(bebasLocalDatasource.getDecryptedEntity().subscribe(
            {
                if (it.base64ImageEktp != null) {
                    _initState.value = InitEktpCameraResult.SuccessLoadData(it.base64ImageEktp!!)
                }
            },
            {
                _initState.value = InitEktpCameraResult.FAILED(BebasException.fromThrowable(it))
            }
        ))
    }
}