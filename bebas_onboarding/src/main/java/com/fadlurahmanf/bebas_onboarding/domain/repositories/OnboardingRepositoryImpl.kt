package com.fadlurahmanf.bebas_onboarding.domain.repositories

import android.content.Context
import android.os.Build
import com.fadlurahmanf.bebas_api.data.datasources.ContentManagementGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.IdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.datasources.OnboardingGuestRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyRequest
import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.data.dto.BebasAppLanguage
import com.fadlurahmanf.bebas_shared.data.dto.EmailModel
import com.fadlurahmanf.bebas_shared.data.dto.OtpModel
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
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

                BebasShared.language = entity.language

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

                val entity = BebasEntity(
                    deviceId = deviceId,
                    encodedPrivateKey = cryptoKey.privateKey,
                    encodedPublicKey = cryptoKey.publicKey
                )

                BebasShared.language = entity.language
                bebasLocalDatasource.insert(entity)
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

    fun requestOtp(phoneNumber: String): Observable<OtpModel> {
        val deviceId = deviceRepository.deviceID(context)

        return onboardingGuestRemoteDatasource.requestOtpAvailability(phoneNumber, deviceId)
            .flatMap { respRequestOtp ->
                if (respRequestOtp.data != null) {
                    val data = respRequestOtp.data!!
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                    parser.timeZone = TimeZone.getTimeZone("UTC")
                    val lastRequestDate = parser.parse(data.lastRequestDate ?: "")
                    val dateNow = Calendar.getInstance().time
                    val diffInMinutes =
                        TimeUnit.MILLISECONDS.toMinutes(
                            dateNow.time - (lastRequestDate?.time ?: 0L)
                        )
                    val diffInSeconds =
                        TimeUnit.MILLISECONDS.toSeconds(
                            dateNow.time - (lastRequestDate?.time ?: 0L)
                        )
                    if ((data.requestAttempt ?: 1) < 3 && diffInSeconds < 60) {
                        return@flatMap Observable.just(
                            OtpModel(
                                remainingOtpInSecond = 60L - diffInSeconds,
                                totalRequestOtpAttempt = data.requestAttempt ?: 1
                            )
                        )
                    } else if ((data.requestAttempt ?: 1) >= 3 && diffInMinutes < 10) {
                        return@flatMap Observable.just(
                            OtpModel(
                                remainingOtpInSecond = 600L - diffInSeconds,
                                totalRequestOtpAttempt = data.requestAttempt ?: 0
                            )
                        )
                    } else {
                        onboardingGuestRemoteDatasource.sendOtpVerification(phoneNumber, deviceId)
                            .map { respSendOtp ->
                                return@map OtpModel(
                                    remainingOtpInSecond = 60L,
                                    totalRequestOtpAttempt = respSendOtp.data?.requestAttempt ?: 1
                                )
                            }
                    }
                } else {
                    onboardingGuestRemoteDatasource.sendOtpVerification(phoneNumber, deviceId)
                        .map { respSendOtp ->
                            return@map OtpModel(
                                remainingOtpInSecond = 60L,
                                totalRequestOtpAttempt = respSendOtp.data?.requestAttempt ?: 1
                            )
                        }
                }
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

            if (it.data?.otpToken == null) {
                throw BebasException.generalRC("OTP_TOKEN_MISSING")
            }

            bebasLocalDatasource.updateOtpToken(it.data!!.otpToken!!)

            it.data!!
        }
    }

    fun getLanguage(): Single<BebasAppLanguage> {
        return bebasLocalDatasource.getLanguage().map {
            val configuration = context.resources.configuration
            val currentLocale = configuration.locale
            BebasAppLanguage(
                it,
                "${currentLocale.language}-${currentLocale.country}"
            )
        }
    }

    fun requestEmail(email: String): Observable<EmailModel> {
        val deviceId = deviceRepository.deviceID(context)
        return bebasLocalDatasource.getDecryptedEntity().toObservable().flatMap { decryptedEntity ->
            val phoneNumber =
                decryptedEntity.phone ?: throw BebasException.generalRC("PHONE_NUMBER_MISSING")
            val otpToken =
                decryptedEntity.otpToken ?: throw BebasException.generalRC("OTP_TOKEN_MISSING")
            val flowType =
                if (decryptedEntity.onboardingFlow == OnboardingFlow.SELF_ACTIVATION) "selfActivation" else "onboarding"
            onboardingGuestRemoteDatasource.requestEmailAvailability(
                email = email,
                phoneNumber = phoneNumber,
                otpToken = otpToken,
                deviceId = deviceId,
                flowType = flowType
            ).flatMap { respRequestEmailVerif ->
                if (respRequestEmailVerif.data == null) {
                    onboardingGuestRemoteDatasource.sendEmailVerification(
                        email = email,
                        phoneNumber = phoneNumber,
                        otpToken = otpToken,
                        deviceId = deviceId,
                        flowType = flowType
                    ).map {
                        EmailModel(
                            remainingEmailInSecond = 60L,
                            totalRequestEmailAttempt = 1
                        )
                    }
                } else {
                    onboardingGuestRemoteDatasource.sendEmailVerification(
                        email = email,
                        phoneNumber = phoneNumber,
                        otpToken = otpToken,
                        deviceId = deviceId,
                        flowType = flowType
                    ).map { baseRespRequestEmail ->
                        val requestAttempt = baseRespRequestEmail.data?.requestAttempt ?: 1

                        val parser =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
                        parser.timeZone = TimeZone.getTimeZone("UTC")
                        val lastRequestDate =
                            parser.parse(baseRespRequestEmail.data?.lastRequestDate ?: "")

                        val dateNow = Calendar.getInstance().time
                        val diffInSeconds =
                            TimeUnit.MILLISECONDS.toSeconds(
                                dateNow.time - (lastRequestDate?.time ?: 0L)
                            )

                        if (requestAttempt < 3 && diffInSeconds < 60L) {
                            EmailModel(
                                remainingEmailInSecond = 60L - diffInSeconds,
                                totalRequestEmailAttempt = requestAttempt
                            )
                        } else if (requestAttempt >= 3 && diffInSeconds < 600L) {
                            EmailModel(
                                remainingEmailInSecond = 600L - diffInSeconds,
                                totalRequestEmailAttempt = requestAttempt
                            )
                        } else {
                            EmailModel(
                                remainingEmailInSecond = 60L,
                                totalRequestEmailAttempt = 1
                            )
                        }
                    }
                }
            }
        }
    }

    fun checkIsEmailVerify(email: String): Observable<CheckEmailIsVerifyResponse> {
        val deviceId = deviceRepository.deviceID(context)
        return bebasLocalDatasource.getDecryptedEntity().toObservable().flatMap { decrypted ->
            val request = CheckEmailIsVerifyRequest(
                email = email,
                deviceId = deviceId,
                deviceModel = Build.MODEL,
                flowType = if (decrypted.onboardingFlow == OnboardingFlow.SELF_ACTIVATION) "selfActivation" else if (decrypted.onboardingFlow == OnboardingFlow.ONBOARDING) "onboarding" else "-",
                phoneNumber = decrypted.phone ?: "",
                appVersion = BebasShared.appVersionName,
                language = decrypted.language,
            )
            onboardingGuestRemoteDatasource.checkEmailIsVerify(request).map { baseResp ->
                if (baseResp.data == null) {
                    throw BebasException.generalRC("DATA_MISSING")
                }

                if (baseResp.data?.emailToken == null) {
                    throw BebasException.generalRC("EMAIL_TOKEN_MISSING")
                }

                bebasLocalDatasource.updateEmailToken(baseResp.data!!.emailToken!!)

                baseResp.data!!
            }
        }
    }
}