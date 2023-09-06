package com.fadlurahmanf.mapp_example.presentation.rtc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.mapp_example.R

class RoomRecycleView : RecyclerView.Adapter<RoomRecycleView.ViewHolder>() {
    private var rooms: ArrayList<String> = arrayListOf()
    private lateinit var callback: Callback

    fun setCallback(callback: Callback){
        this.callback = callback
    }

    fun setList(list: List<String>) {
        this.rooms.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomTitle: TextView = view.findViewById(R.id.tv_room_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_room, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rooms.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = rooms[position]

        holder.roomTitle.text = room
        holder.itemView.setOnClickListener {
            callback.onClicked(room)
        }
    }

    interface Callback {
        fun onClicked(roomId:String)
    }
}