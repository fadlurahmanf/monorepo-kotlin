package com.fadlurahmanf.core_crypto.external.helper

import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import com.fadlurahmanf.core_crypto.data.dto.exception.CryptoException
import android.util.Base64

abstract class BaseCrypto {

    open fun encode(byte: ByteArray): String {
        return Base64.encodeToString(byte, Base64.NO_WRAP)
    }

    open fun decode(text: String): ByteArray {
        return Base64.decode(text.toByteArray(), Base64.NO_WRAP)
    }

    open fun decode(byte: ByteArray): ByteArray {
        return Base64.decode(byte, Base64.NO_WRAP)
    }

    open fun getPaddingScheme(scheme: PaddingScheme): String {
        return when (scheme) {
            PaddingScheme.PKCS1 -> {
                "PKCS1Padding"
            }

            PaddingScheme.PKCS5 -> {
                "PKCS5Padding"
            }

            PaddingScheme.PKCS7 -> {
                "PKCS7Padding"
            }

            PaddingScheme.PKCS8 -> {
                "PKCS8Padding"
            }

            PaddingScheme.NoPadding -> {
                "NoPadding"
            }

            else -> {
                throw CryptoException("PADDING SCHEME NOT FOUND")
            }
        }
    }
}