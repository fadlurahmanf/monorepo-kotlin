package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class PrepareOnboardingViewModel @Inject constructor(
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {
    fun initPrepareOnboarding(){
        compositeDisposable().add(bebasLocalDatasource.getDecryptedEntity().subscribe(
            {

            },
            {}
        ))
    }

    fun updateIsFinishedPreparedOnBoarding(isFinished: Boolean) {
        compositeDisposable().add(bebasLocalDatasource.updateIsFinishedPrepareOnboarding(isFinished))
    }
}