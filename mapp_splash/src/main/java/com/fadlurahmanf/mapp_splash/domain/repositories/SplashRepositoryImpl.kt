package com.fadlurahmanf.mapp_splash.domain.repositories

import com.fadlurahmanf.mapp_api.data.datasources.remote.MasIdentityRemoteDatasource
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    val identityDatasource: MasIdentityRemoteDatasource
) {
}