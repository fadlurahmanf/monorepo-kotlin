package com.fadlurahmanf.mapp_fcm.domain.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging

class MappFcmRepositoryImpl : MappFcmRepository {
    override fun getFcmToken(): Task<String> {
        return FirebaseMessaging.getInstance().token
    }
}