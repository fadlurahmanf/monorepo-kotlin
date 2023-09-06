package com.fadlurahmanf.mapp_firebase_database.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class MappFirebaseDatabaseRepositoryImpl @Inject constructor() {
    private val reference: DatabaseReference = Firebase.database.reference

    fun getVideoCall2Reference():DatabaseReference{
        return reference.child("videocall_2")
    }

    fun getVideoCall2Snapshot(): Task<DataSnapshot> {
        return reference.child("videocall_2").get()
    }
}