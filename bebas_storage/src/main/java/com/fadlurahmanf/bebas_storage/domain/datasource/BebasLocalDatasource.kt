package com.fadlurahmanf.bebas_storage.domain.datasource

import android.content.Context
import com.fadlurahmanf.bebas_shared.data.enum_class.OnboardingFlow
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

    private fun getEntity(): Single<BebasEntity> {
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

    fun updateLanguage(language: String) {
        val entitySubscriber = getEntity().map { entity ->
            dao.update(entity.copy(language = language))
        }
        entitySubscriber.subscribe()
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
    fun isDataExist(): Single<Boolean> {
        return dao.getAll().map {
            it.isNotEmpty()
        }
    }

    fun delete() = dao.delete()
}