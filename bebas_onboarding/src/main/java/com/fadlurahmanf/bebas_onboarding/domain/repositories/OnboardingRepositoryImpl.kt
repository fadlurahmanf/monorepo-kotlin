package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.ContentManagementGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.IdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.OnboardingGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpResponse
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpResponse
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.dto.OtpModel
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val context: Context,
    private val identityRemoteDatasource: IdentityRemoteDatasource,
    private val contentManagementGuestRemoteDatasource: ContentManagementGuestRemoteDatasource,
    private val onboardingGuestRemoteDatasource: OnboardingGuestRemoteDatasource,
    private val bebasLocalDatasource: BebasLocalDatasource,
    private val cryptoRSARepository: CryptoRSARepository,
    private val deviceRepository: DeviceRepository,
) {

    fun logConsole() = (context.applicationContext as BebasApplication).logConsole
    fun generateCryptoKeyOrFetchTheExisting(): Single<Boolean> {
        return bebasLocalDatasource.getAll().map {
            if (it.isNotEmpty()) {
                val entity = it.first()
                if (entity.encodedPrivateKey != null && entity.encodedPublicKey != null) {
                    BebasShared.setCryptoKey(
                        encodedPublicKey = entity.encodedPublicKey!!,
                        encodedPrivateKey = entity.encodedPrivateKey!!
                    )
                }
            } else {
                val cryptoKey = cryptoRSARepository.generateKey()
                BebasShared.setCryptoKey(
                    encodedPrivateKey = cryptoKey.privateKey,
                    encodedPublicKey = cryptoKey.publicKey
                )

                val deviceId = deviceRepository.deviceID(context)
                bebasLocalDatasource.insert(
                    BebasEntity(
                        deviceId = deviceId,
                        encodedPrivateKey = cryptoKey.privateKey,
                        encodedPublicKey = cryptoKey.publicKey
                    )
                )
            }
            true
        }
    }

    fun generateGuestToken(): Observable<CreateGuestTokenResponse> {
        val guestId = deviceRepository.randomUUID()
        val request = GenerateGuestTokenRequest(
            guestId = guestId
        )
        return identityRemoteDatasource.generateGuestToken(request).map {
            if (it.data?.accessToken == null) {
                throw BebasException.generalRC("GUEST_TOKEN_DATA_MISSING")
            }

            val guestToken = it.data?.accessToken ?: ""
            BebasShared.setGuestToken(guestToken)
            bebasLocalDatasource.updateGuestToken(guestToken)

            it.data!!
        }
    }

    fun getWelcomeBanner() = contentManagementGuestRemoteDatasource.getWelcomeBanner().map {
        if (it.data == null) {
            throw BebasException.generalRC("BANNER_MISSING")
        }
        it.data!!
    }

    fun getTNC() = contentManagementGuestRemoteDatasource.getTNC().map {
        if (it.data == null) {
            throw BebasException.generalRC("TNC_MISSING")
        }
        it.data!!
    }

    fun getLanguage(): Single<String> {
        return bebasLocalDatasource.getLanguage()
    }

    fun switchLanguage(language: String) {
        bebasLocalDatasource.updateLanguage(language)
    }

    fun requestOtp(phoneNumber: String): Observable<OtpModel> {
        val deviceId = deviceRepository.deviceID(context)
        return onboardingGuestRemoteDatasource.requestOtpAvailability(phoneNumber, deviceId).map {
            return@map OtpModel(
                remainingOtpInSecond = 60,
                totalRequestOtpAttempt = 1
            )
        }
    }

    fun verifyOtp(otp: String, phoneNumber: String): Observable<VerifyOtpResponse> {
        val deviceId = deviceRepository.deviceID(context)
        val request = VerifyOtpRequest(
            phoneNumber, deviceId, otp
        )
        return onboardingGuestRemoteDatasource.verifyOtp(request).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            if (it.data?.isVerify != true) {
                throw BebasException(
                    rawMessage = "OTP_FALSE"
                )
            }

            it.data!!
        }
    }
}