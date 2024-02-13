package com.fadlurahmanf.bebas_fcm.domain.repositories

import io.reactivex.rxjava3.core.Observable

interface BebasFcmRepository {
    fun getFcmToken(): Observable<String>
}