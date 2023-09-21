package com.fadlurahmanf.core_crypto.domain.repositories

import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import com.fadlurahmanf.core_crypto.data.dto.model.CryptoKey
import java.security.PrivateKey
import java.security.PublicKey

interface CryptoRSARepository {
    fun generateKey(): CryptoKey
    fun loadPublicKey(encodedPublicKey: String): PublicKey
    fun loadPrivateKey(encodedPrivateKey: String): PrivateKey
    fun encrypt(
        plainText: String,
        encodedPublicKey: String,
        paddingScheme: PaddingScheme = PaddingScheme.PKCS1
    ): String?

    fun decrypt(
        encrypted: String,
        encodedPrivateKey: String,
        paddingScheme: PaddingScheme = PaddingScheme.PKCS1
    ): String?

    fun createSignature(encodedPrivateKey: String, text: String): String
    fun verifySignature(encodedPublicKey: String, text: String, signature: String): Boolean
}