package com.fadlurahmanf.mapp_example.presentation.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.data.dto.model.MenuModel

class MenuRecycleView : RecyclerView.Adapter<MenuRecycleView.ViewHolder>() {
    private val menus: ArrayList<MenuModel> = arrayListOf()
    private lateinit var callBack: CallBack

    fun setList(list: ArrayList<MenuModel>) {
        menus.clear()
        menus.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById<TextView>(R.id.tv_title)
//        val subTitle: TextView = view.findViewById<TextView>(R.id.tv_sub_title)
//        val icon: ImageView = view.findViewById<ImageView>(R.id.iv_start)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_menu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = menus.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menus[position]

        holder.title.text = menu.menuTitle
        holder.itemView.setOnClickListener {
            callBack.onMenuClicked(menu)
        }
    }

    interface CallBack {
        fun onMenuClicked(menu: MenuModel)
    }
}