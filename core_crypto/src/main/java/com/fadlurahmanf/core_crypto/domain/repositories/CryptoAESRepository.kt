package com.fadlurahmanf.core_crypto.domain.repositories

import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import java.util.Random
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

interface CryptoAESRepository {
    fun generateKey(): String
    fun encryptECB(
        plainText: String, secretKey: String, padding: PaddingScheme = PaddingScheme.PKCS7
    ): String?

    fun decryptECB(
        encryptedText: String, secretKey: String, padding: PaddingScheme = PaddingScheme.PKCS7
    ): String?

    fun encryptCBC(
        plainText: String, secretKey: String, padding: PaddingScheme = PaddingScheme.PKCS7
    ): String?

    fun decryptCBC(
        encryptedText: String, secretKey: String, padding: PaddingScheme = PaddingScheme.PKCS7
    ): String?
}