package com.fadlurahmanf.mapp_splash.presentation.view_model

import com.fadlurahmanf.mapp_splash.domain.repositories.SplashRepositoryImpl
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repositoryImpl: SplashRepositoryImpl
) {
}