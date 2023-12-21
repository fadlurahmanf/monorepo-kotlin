package com.fadlurahmanf.bebas_transaction.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119Repository
import javax.inject.Inject

class CryptoTransactionRepositoryImpl @Inject constructor(
    context: Context,
    private val cryptoED25119Repository: CryptoED25119Repository
) {

    fun verifyPin(plainPin: String): Boolean {
        val signature = cryptoED25119Repository.generateSignature(
            plainPin,
            BebasShared.encodedPrivateKeyTransaction
        ) ?: return false
        return cryptoED25119Repository.verifySignature(
            text = plainPin,
            encodedPublicKey = BebasShared.encodedPublicKeyTransaction,
            signature = signature
        )
    }

}