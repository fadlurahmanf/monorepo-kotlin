package com.fadlurahmanf.core_platform.domain

import android.content.Context
import javax.inject.Inject

class BiometricFakeRepositoryImpl : BiometricRepository {
    override fun isSupportedBiometric(context: Context): Boolean {
        return false
    }
}