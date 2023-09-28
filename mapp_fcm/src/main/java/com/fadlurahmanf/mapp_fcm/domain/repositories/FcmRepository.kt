package com.fadlurahmanf.mapp_fcm.domain.repositories

import com.google.android.gms.tasks.Task

interface FcmRepository {
    fun getFcmToken(): Task<String>
}