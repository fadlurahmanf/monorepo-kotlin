package com.fadlurahmanf.bebas_storage.domain.datasource

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_storage.data.entity.BebasDecryptedEntity
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.common.BebasDatabase
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import java.lang.Exception
import javax.inject.Inject

class BebasLocalDatasource @Inject constructor(
    context: Context,
    private val coreRSARepository: CryptoRSARepository
) {
    private var dao = BebasDatabase.getDatabase(context).bebasDao()

    fun getAll() = dao.getAll()

    fun insert(value: BebasEntity) {
        dao.insert(value)
    }

    fun insertOrReplaceWithExisting(value: BebasEntity) {
        dao.insert(value)
    }

    fun delete() = dao.delete()

    fun getEntity(): Single<BebasEntity> {
        return dao.getAll().map {
            if (it.isEmpty()) {
                throw BebasException.generalRC("ENTITY_MISSING")
            }

            val entity = it.first()
            entity
        }
    }

    fun updateGuestToken(guestToken: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedGuestToken =
                coreRSARepository.encrypt(guestToken, entity.encodedPublicKey ?: "")
            dao.update(entity.copy(encryptedGuestToken = encryptedGuestToken))
        }
        return entitySubscriber
    }

    fun updateLastScreen(screen: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(lastScreen = screen))
        }
        return entitySubscriber
    }

    fun updateFlowOnboarding(flow: OnboardingFlow): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(onboardingFlow = flow))
        }
        return entitySubscriber
    }

    fun updateIsFinishedReadTNC(value: Boolean): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedReadTnc = value))
        }
        return entitySubscriber
    }

    fun removeOnboardingFlow(): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(onboardingFlow = null))
        }
        return entitySubscriber
    }

    fun getLanguage(): Single<String> {
        return getEntity().map { entity ->
            entity.language
        }
    }

    fun updateLanguage(language: String): Single<Unit> {
        return getEntity().map { entity ->
            BebasShared.language = language
            RxBus.publish(
                RxEvent.ChangeLanguageEvent(
                    languageCode = language.split("-").first(),
                    countryCode = language.split("-").last(),
                    language = language
                )
            )
            dao.update(entity.copy(language = language))
        }
    }

    fun updatePhoneAndEmailAndReturn(phone: String, email: String): Single<BebasEntity> {
        val subscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedPhone =
                coreRSARepository.encrypt(phone, entity.encodedPublicKey ?: "")
            val encryptedEmail =
                coreRSARepository.encrypt(email, entity.encodedPublicKey ?: "")
            val newEntity =
                entity.copy(encryptedPhone = encryptedPhone, encryptedEmail = encryptedEmail)
            dao.update(newEntity)
            newEntity
        }
        return subscriber
    }

    fun deletePhoneAndEmail(): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(
                entity.copy(
                    encryptedPhone = null,
                    encryptedEmail = null,
                    encryptedOtpToken = null,
                    encryptedEmailToken = null
                )
            )
        }
        return entitySubscriber
    }


    fun getDecryptedEntity() = dao.getAll().map { entities ->
        val entity = entities.first()
        val privateKey = entity.encodedPrivateKey
        val decryptedEntity = BebasDecryptedEntity(
            deviceId = entity.deviceId,
            language = entity.language,
            publicKey = entity.encodedPublicKey,
            privateKey = entity.encodedPrivateKey,
            guestToken = decrypt(entity.encryptedGuestToken, privateKey),
            accessToken = decrypt(entity.encryptedAccessToken, privateKey),
            refreshToken = decrypt(entity.encryptedRefreshToken, privateKey),
            expiresAt = entity.expiresAt,
            refreshExpiresAt = entity.refreshExpiresAt,
            onboardingFlow = entity.onboardingFlow,
            phone = decrypt(entity.encryptedPhone, privateKey),
            email = decrypt(entity.encryptedEmail, privateKey),
            isFinishedReadTnc = entity.isFinishedReadTnc,
            lastScreen = entity.lastScreen,
            otpToken = decrypt(entity.encryptedOtpToken, privateKey),
            emailToken = decrypt(entity.encryptedEmailToken, privateKey),
            onboardingId = decrypt(entity.encryptedOnboardingId, privateKey),
            isFinishedOtpVerification = entity.isFinishedOtpVerification,
            isFinishedEmailVerification = entity.isFinishedEmailVerification,
            base64ImageEktp = entity.base64ImageEktp,
            isFinishedEktpCameraVerification = entity.isFinishedEktpCameraVerification,
            idCardNumber = decrypt(entity.encryptedIdCardNumber, privateKey),
            fullName = decrypt(entity.encryptedFullName, privateKey),
            birthPlace = entity.birthPlace,
            birthDate = entity.birthDate,
            gender = entity.gender,
            province = decrypt(entity.encryptedProvince, privateKey),
            city = decrypt(entity.encryptedCity, privateKey),
            subDistrict = decrypt(entity.encryptedSubDistrict, privateKey),
            ward = decrypt(entity.encryptedWard, privateKey),
            address = decrypt(entity.encryptedAddress, privateKey),
            rtRw = decrypt(entity.encryptedRtRw, privateKey)
        )
        decryptedEntity
    }

    fun isDataExist(): Single<Boolean> {
        return dao.getAll().map {
            it.isNotEmpty()
        }
    }

    fun updateOtpToken(otpToken: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedOtpToken =
                coreRSARepository.encrypt(otpToken, entity.encodedPublicKey ?: "")
            dao.update(entity.copy(encryptedOtpToken = encryptedOtpToken))
        }
        return entitySubscriber
    }

    fun updateEmailToken(emailToken: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedEmailToken =
                coreRSARepository.encrypt(emailToken, entity.encodedPublicKey ?: "")
            dao.update(entity.copy(encryptedEmailToken = encryptedEmailToken))
        }
        return entitySubscriber
    }

    fun updateEmailTokenAndOnboardingId(emailToken: String, onboardingId: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedEmailToken =
                coreRSARepository.encrypt(emailToken, entity.encodedPublicKey ?: "")
            val encryptedOnboardingId =
                coreRSARepository.encrypt(onboardingId, entity.encodedPublicKey ?: "")
            dao.update(
                entity.copy(
                    encryptedEmailToken = encryptedEmailToken,
                    encryptedOnboardingId = encryptedOnboardingId
                )
            )
        }
        return entitySubscriber
    }

    fun updateIsFinishedOtpVerification(isFinished: Boolean?): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedOtpVerification = isFinished))
        }
        return entitySubscriber
    }

    fun updateIsFinishedEmailVerification(isFinished: Boolean?): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedEmailVerification = isFinished))
        }
        return entitySubscriber
    }

    fun updateIsFinishedPrepareOnboarding(isFinished: Boolean?): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedPrepareOnboarding = isFinished))
        }
        return entitySubscriber
    }

    fun updateBase64ImageEktp(base64Image: String): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(base64ImageEktp = base64Image))
        }
        return entitySubscriber
    }

    fun updateOcrOobData(
        nik: String? = null,
        fullName: String? = null,
        birthPlace: String? = null,
        birthDate: String? = null,
        gender: String? = null,
        province: String? = null,
        city: String? = null,
        subDistrict: String? = null,
        ward: String? = null,
        address: String? = null,
        rtRw: String? = null,
    ): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            val publicKey = entity.encodedPublicKey
            val newEntity = entity.copy(
                encryptedIdCardNumber = encrypt(nik, publicKey),
                encryptedFullName = encrypt(fullName, publicKey),
                birthPlace = birthPlace,
                birthDate = birthDate,
                gender = gender,
                encryptedProvince = encrypt(province, publicKey),
                encryptedCity = encrypt(city, publicKey),
                encryptedSubDistrict = encrypt(subDistrict, publicKey),
                encryptedWard = encrypt(ward, publicKey),
                encryptedAddress = encrypt(address, publicKey),
                encryptedRtRw = encrypt(rtRw, publicKey)
            )
            dao.update(newEntity)
        }
        return entitySubscriber
    }

    fun updateIsFinishedEktpCameraVerification(value: Boolean?): Single<Unit> {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedEktpCameraVerification = value))
        }
        return entitySubscriber
    }

    private fun encrypt(plain: String?, publicKey: String?): String? {
        return if (plain != null) {
            coreRSARepository.encrypt(
                plain,
                publicKey ?: ""
            )
        } else {
            null
        }
    }

    private fun decrypt(encrypted: String?, privateKey: String?): String? {
        return if (encrypted != null) {
            coreRSARepository.decrypt(
                encrypted,
                privateKey ?: ""
            )
        } else {
            null
        }
    }
}