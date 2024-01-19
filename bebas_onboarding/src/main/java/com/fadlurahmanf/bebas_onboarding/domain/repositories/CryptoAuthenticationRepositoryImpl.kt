package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoAESRepository
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119Repository
import javax.inject.Inject

class CryptoAuthenticationRepositoryImpl @Inject constructor(
    context: Context,
    private val cryptoAESRepository: CryptoAESRepository,
    private val cryptoED25119Repository: CryptoED25119Repository,
) {

    fun encryptPassword(password: String): String? {
        val saltPassword = BebasShared.saltPassword
        Log.d("BebasLogger", "SALT PASS: $saltPassword")
        Log.d("BebasLogger", "SALT PASS LENGTH: ${saltPassword.length}")
        return cryptoAESRepository.encryptCBC(
            plainText = password,
            secretKey = saltPassword,
        )
    }

}