package com.fadlurahmanf.core_crypto.external.helper

import android.os.Build
import com.fadlurahmanf.core_crypto.data.dto.enum.PaddingScheme
import com.fadlurahmanf.core_crypto.data.dto.exception.CryptoException
import java.util.Base64

abstract class BaseCrypto {

    open fun encode(byte: ByteArray): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(byte)
        } else {
            android.util.Base64.encodeToString(byte, android.util.Base64.DEFAULT)
        }
    }

    open fun decode(text: String): ByteArray {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(text.toByteArray())
        } else {
            android.util.Base64.decode(text.toByteArray(), android.util.Base64.DEFAULT)
        }
    }

    open fun decode(byte: ByteArray): ByteArray {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(byte)
        } else {
            android.util.Base64.decode(byte, android.util.Base64.DEFAULT)
        }
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