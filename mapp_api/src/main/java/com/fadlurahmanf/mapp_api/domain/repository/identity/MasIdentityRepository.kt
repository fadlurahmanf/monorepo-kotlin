package com.fadlurahmanf.mapp_api.domain.repository.identity

import com.fadlurahmanf.mapp_api.data.datasources.remote.MasIdentityRemoteDatasource
import javax.inject.Inject

class MasIdentityRepository @Inject constructor(
    var remoteDatasource: MasIdentityRemoteDatasource
) {
}