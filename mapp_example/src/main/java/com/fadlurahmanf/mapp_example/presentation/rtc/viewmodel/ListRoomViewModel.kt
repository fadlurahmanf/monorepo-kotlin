package com.fadlurahmanf.mapp_example.presentation.rtc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.mapp_firebase_database.domain.MappFirebaseDatabaseRepositoryImpl
import javax.inject.Inject

class ListRoomViewModel @Inject constructor(
    var databaseRepository: MappFirebaseDatabaseRepositoryImpl
) {
    init {
        getListRoom()
    }

    private val _rooms = MutableLiveData<List<String>>()
    val rooms: LiveData<List<String>> = _rooms

    private fun getListRoom() {
        databaseRepository.getVideoCall2Reference().child("rooms").get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.children.map {
                    it.key ?: "-"
                }.toList()
                _rooms.value = list
            }
    }
}