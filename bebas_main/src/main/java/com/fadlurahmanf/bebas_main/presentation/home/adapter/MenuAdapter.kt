package com.fadlurahmanf.bebas_main.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.home.TransactionMenuModel

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    lateinit var context: Context
    private val menus: ArrayList<TransactionMenuModel> = arrayListOf()

    fun setList(list: List<TransactionMenuModel>) {
        menus.clear()
        menus.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.iv_menu)
        val label = view.findViewById<TextView>(R.id.tv_menu_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (!::context.isInitialized) {
            context = parent.context
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction_menu, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = menus.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menus[position]

        holder.label.text = context.getString(menu.menuLabel)
        Glide.with(holder.image).load(ContextCompat.getDrawable(context, menu.imageMenu)).into(holder.image)
    }
}