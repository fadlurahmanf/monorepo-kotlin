package com.fadlurahmanf.mapp_fcm.domain.repositories

import com.google.android.gms.tasks.Task

interface MappFcmRepository {
    fun getFcmToken(): Task<String>
}