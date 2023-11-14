package com.fadlurahmanf.bebas_shared

import android.util.Log

object BebasShared {
    lateinit var flavor: String
    private lateinit var bebasUrl: String

    fun setBebasUrl(url: String) {
        if (!this::bebasUrl.isInitialized) {
            bebasUrl = url
        }
    }

    fun getBebasUrl(): String {
        return bebasUrl
    }

    private lateinit var encodedPublicKey: String
    private lateinit var encodedPrivateKey: String

    fun setCryptoKey(encodedPrivateKey: String, encodedPublicKey: String) {
        if (!this::encodedPrivateKey.isInitialized && !this::encodedPublicKey.isInitialized) {
            this.encodedPrivateKey = encodedPrivateKey
            this.encodedPublicKey = encodedPublicKey
        }
    }

    private lateinit var guestToken: String

    fun setGuestToken(guestToken: String) {
        this.guestToken = guestToken
    }

    fun getGuestToken(): String {
        return guestToken
    }

    lateinit var language: String
}