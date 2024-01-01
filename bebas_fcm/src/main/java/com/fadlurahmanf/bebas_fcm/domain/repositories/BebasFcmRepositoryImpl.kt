package com.fadlurahmanf.bebas_fcm.domain.repositories

import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.core.Observable

class BebasFcmRepositoryImpl : BebasFcmRepository {
    override fun getFcmToken(): Observable<String> {
        return Observable.create<String> { emitter ->
            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                emitter.onNext(it)
            }.addOnFailureListener {
                emitter.onError(it)
            }.addOnCompleteListener {
                emitter.onComplete()
            }
        }
    }
}