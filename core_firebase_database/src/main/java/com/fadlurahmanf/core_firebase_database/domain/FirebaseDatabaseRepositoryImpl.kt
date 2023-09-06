package com.fadlurahmanf.core_firebase_database.domain

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

open class FirebaseDatabaseRepositoryImpl @Inject constructor() : FirebaseDatabaseRepository {
    val database: DatabaseReference = Firebase.database.reference
}