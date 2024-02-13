package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.data.state.InitPrepareOnboardingState
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class PrepareOnboardingViewModel @Inject constructor(
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    private val _initState = MutableLiveData<InitPrepareOnboardingState>()
    val initState: LiveData<InitPrepareOnboardingState> = _initState
    fun initPrepareOnboarding() {
        compositeDisposable().add(bebasLocalDatasource.getDecryptedEntity().subscribe(
            {
                if (it.isFinishedEktpCameraVerification == true && it.base64ImageEktp != null) {
                    _initState.value = InitPrepareOnboardingState.SuccessToEktpVerification
                } else if (it.isFinishedPrepareOnboarding == true) {
                    _initState.value = InitPrepareOnboardingState.SuccessToEktpCamera
                }
            },
            {
                _initState.value =
                    InitPrepareOnboardingState.FAILED(BebasException.fromThrowable(it))
            }
        ))
    }

    fun updateIsFinishedPreparedOnBoarding(isFinished: Boolean) {
        compositeDisposable().add(bebasLocalDatasource.updateIsFinishedPrepareOnboarding(isFinished).subscribe())
    }
}