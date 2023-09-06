package com.fadlurahmanf.mapp_example.presentation.rtc

import android.content.Intent
import com.fadlurahmanf.mapp_example.databinding.ActivityListRoomBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.rtc.viewmodel.ListRoomViewModel
import javax.inject.Inject

class ListRoomActivity :
    BaseExampleActivity<ActivityListRoomBinding>(ActivityListRoomBinding::inflate),
    RoomRecycleView.Callback {
    override fun injectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: ListRoomViewModel

    private lateinit var adapter:RoomRecycleView
    private val list = arrayListOf<String>()
    override fun setup() {
        adapter = RoomRecycleView()
        adapter.setCallback(this)
        adapter.setList(list)
        binding.rvRoom.adapter = adapter

        viewModel.rooms.observe(this){
            list.clear()
            list.addAll(it)
            adapter.setList(list)
        }
    }

    override fun onClicked(roomId: String) {
        val intent = Intent(this, CallActivity::class.java)
        startActivity(intent)
    }

}