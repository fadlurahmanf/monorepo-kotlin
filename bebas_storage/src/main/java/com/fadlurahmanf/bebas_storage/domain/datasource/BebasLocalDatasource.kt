package com.fadlurahmanf.bebas_storage.domain.datasource

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
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

    fun updateGuestToken(guestToken: String) {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedGuestToken =
                coreRSARepository.encrypt(guestToken, entity.encodedPublicKey ?: "")
            dao.update(entity.copy(encryptedGuestToken = encryptedGuestToken))
        }
        entitySubscriber.subscribe()
    }

    fun updateLastScreen(screen: String): Disposable {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(lastScreen = screen))
        }
        return entitySubscriber.subscribe()
    }

    fun updateFlowOnboarding(flow: OnboardingFlow): Disposable {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(onboardingFlow = flow))
        }
        return entitySubscriber.subscribe()
    }

    fun updateIsFinishedReadTNC(value: Boolean): Disposable {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(isFinishedReadTnc = value))
        }
        return entitySubscriber.subscribe()
    }

    fun removeOnboardingFlow(): Disposable {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(onboardingFlow = null))
        }
        return entitySubscriber.subscribe()
    }

    fun getLanguage(): Single<String> {
        return getEntity().map { entity ->
            entity.language
        }
    }

    fun updateLanguage(language: String): Single<Unit> {
        return getEntity().map { entity ->
            BebasShared.language = language
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
            otpToken = decrypt(entity.encryptedOtpToken, privateKey)
        )
        decryptedEntity
    }

    fun isDataExist(): Single<Boolean> {
        return dao.getAll().map {
            it.isNotEmpty()
        }
    }

    fun updateOtpToken(otpToken: String) {
        val entitySubscriber = getEntity().map { entity ->
            if (entity.encodedPublicKey == null) {
                throw Exception()
            }
            val encryptedOtpToken =
                coreRSARepository.encrypt(otpToken, entity.encodedPublicKey ?: "")
            dao.update(entity.copy(encryptedOtpToken = encryptedOtpToken))
        }
        entitySubscriber.subscribe()
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