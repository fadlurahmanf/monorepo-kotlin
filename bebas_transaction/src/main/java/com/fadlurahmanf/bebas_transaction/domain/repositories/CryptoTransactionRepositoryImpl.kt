package com.fadlurahmanf.bebas_transaction.domain.repositories

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119Repository
import com.google.gson.Gson
import org.json.JSONObject
import javax.inject.Inject

class CryptoTransactionRepositoryImpl @Inject constructor(
    context: Context,
    private val cryptoED25119Repository: CryptoED25119Repository
) {

    fun generateSignature(
        plainJsonString: String,
        timestamp: String,
        challengeCode: String
    ): String {
        val unsign = "$plainJsonString|$timestamp|$challengeCode"
        Log.d("BebasLogger", "UNSIGN: $unsign")
        return cryptoED25119Repository.generateSignature(
            unsign,
            BebasShared.encodedPrivateKeyTransaction
        ) ?: ""
    }

    fun verifyPin(plainPin: String): Boolean {
        val signature = cryptoED25119Repository.generateSignature(
            plainPin,
            BebasShared.encodedPrivateKeyTransaction
        ) ?: return false
        Log.d("BebasLogger", "VERIFY PIN SIGNATURE: $signature")
        val isPinVerify = cryptoED25119Repository.verifySignature(
            text = plainPin,
            encodedPublicKey = BebasShared.encodedPublicKeyTransaction,
            signature = signature
        )
        Log.d("BebasLogger", "IS PIN VERIFY: $isPinVerify")
        return isPinVerify
    }

    fun verifySignature(message: String, signature: String): Boolean {
        return cryptoED25119Repository.verifySignature(
            text = message,
            encodedPublicKey = BebasShared.encodedPublicKeyTransaction,
            signature = signature
        )
    }

}