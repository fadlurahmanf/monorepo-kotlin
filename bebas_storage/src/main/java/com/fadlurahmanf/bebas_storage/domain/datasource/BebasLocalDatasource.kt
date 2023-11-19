package com.fadlurahmanf.bebas_storage.domain.datasource

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_shared.BebasShared
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

    fun insert(value: BebasEntity) {
        dao.insert(value)
    }

    fun insertOrReplaceWithExisting(value: BebasEntity) {
        dao.insert(value)
    }

    fun getEntity(): Single<BebasEntity> {
        return dao.getAll().map {
            if (it.isEmpty()) {
                throw Exception()
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

    fun getAll() = dao.getAll()


    fun getDecryptedEntity() = dao.getAll().map { entities ->
        val entity = entities.first()
        val key = entity.encodedPrivateKey ?: ""
        var guestToken: String? = null
        if (entity.encryptedGuestToken != null) {
            guestToken = coreRSARepository.decrypt(
                entity.encryptedGuestToken ?: "",
                entity.encodedPrivateKey ?: ""
            )
        }
        var accessToken: String? = null
        if (entity.encryptedAccessToken != null) {
            accessToken = coreRSARepository.decrypt(
                entity.encryptedAccessToken ?: "",
                entity.encodedPrivateKey ?: ""
            )
        }
        var refreshToken: String? = null
        if (entity.encryptedRefreshToken != null) {
            refreshToken = coreRSARepository.decrypt(
                entity.encryptedRefreshToken ?: "",
                entity.encodedPrivateKey ?: ""
            )
        }
        var phone: String? = null
        if (entity.encryptedPhone != null) {
            phone = coreRSARepository.decrypt(
                entity.encryptedPhone ?: "",
                entity.encodedPrivateKey ?: ""
            )
        }
        var email: String? = null
        if (entity.encryptedEmail != null) {
            email = coreRSARepository.decrypt(
                entity.encryptedEmail ?: "",
                entity.encodedPrivateKey ?: ""
            )
        }
        val decryptedEntity = BebasDecryptedEntity(
            deviceId = entity.deviceId,
            language = entity.language,
            publicKey = entity.encodedPublicKey,
            privateKey = entity.encodedPrivateKey,
            guestToken = guestToken,
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresAt = entity.expiresAt,
            refreshExpiresAt = entity.refreshExpiresAt,
            onboardingFlow = entity.onboardingFlow,
            phone = phone,
            email = email,
            isFinishedReadTnc = entity.isFinishedReadTnc,
            lastScreen = entity.lastScreen
        )
        decryptedEntity
    }

    fun isDataExist(): Single<Boolean> {
        return dao.getAll().map {
            it.isNotEmpty()
        }
    }

    fun delete() = dao.delete()
}