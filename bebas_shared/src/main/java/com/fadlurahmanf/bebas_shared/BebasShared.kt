package com.fadlurahmanf.bebas_shared

import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel

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


    lateinit var appVersionCode: String

    lateinit var appVersionName: String

    lateinit var packageId: String

    val genderItems: List<BebasItemPickerBottomsheetModel> =
        if (::language.isInitialized && language == "en-US") listOf(
            BebasItemPickerBottomsheetModel(
                id = "F",
                label = "FEMALE",
            ),
            BebasItemPickerBottomsheetModel(
                id = "M",
                label = "MALE",
            )
        ) else listOf(
            BebasItemPickerBottomsheetModel(
                id = "F",
                label = "PEREMPUAN",
            ),
            BebasItemPickerBottomsheetModel(
                id = "M",
                label = "LAKI-LAKI",
            )
        )


    // OPEN VIDU
    private lateinit var openviduBaseUrl: String
    private lateinit var openviduHost: String

    fun setOpenviduBaseUrl(url: String, host:String) {
        if (!this::openviduBaseUrl.isInitialized) {
            openviduBaseUrl = url
        }

        if (!this::openviduHost.isInitialized){
            openviduHost = host
        }
    }

    fun getOpenviduBaseUrl(): String {
        return openviduBaseUrl
    }

    fun getOpenviduHost(): String {
        return openviduHost
    }

    var openviduBasicHeader: String = "OPENVIDUAPP:QkFOS01BUzIwMjIK"
}

