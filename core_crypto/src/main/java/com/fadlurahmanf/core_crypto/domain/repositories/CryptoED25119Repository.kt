package com.fadlurahmanf.core_crypto.domain.repositories

import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import com.fadlurahmanf.core_crypto.data.dto.model.CryptoKey
import java.util.Random
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

interface CryptoED25119Repository {
    fun generateKey(): CryptoKey

    fun generateSignature(plainText: String, encodedPrivateKey: String): String?

    fun verifySignature(text: String, signature: String, encodedPublicKey: String): Boolean
}