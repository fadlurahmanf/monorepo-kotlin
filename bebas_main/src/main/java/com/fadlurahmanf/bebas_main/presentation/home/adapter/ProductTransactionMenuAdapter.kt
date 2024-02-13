package com.fadlurahmanf.bebas_main.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.model.home.ProductTransactionMenuModel

class ProductTransactionMenuAdapter(
    private val type: Type
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class Type {
        HOME_PRODUCT_TRANSACTION_MENU, BOTTOMSHEET_TRANSACTION_MENU_LABEL
    }

    lateinit var context: Context
    private val menus: ArrayList<ProductTransactionMenuModel> = arrayListOf()
    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setList(list: List<ProductTransactionMenuModel>) {
        menus.clear()
        menus.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun changeData(menuModel: ProductTransactionMenuModel, index: Int) {
        notifyItemChanged(index, menuModel)
    }

    inner class HomeProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.iv_menu)
        val label = view.findViewById<TextView>(R.id.tv_menu_label)
    }

    inner class BottomsheetProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.findViewById<TextView>(R.id.tv_label)
        val main = view.findViewById<LinearLayout>(R.id.ll_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }
        return if (type == Type.HOME_PRODUCT_TRANSACTION_MENU) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_menu, parent, false)
            HomeProductViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction_menu_label, parent, false)
            BottomsheetProductViewHolder(view)
        }
    }

    override fun getItemCount(): Int = menus.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menu = menus[position]

        if (type == Type.HOME_PRODUCT_TRANSACTION_MENU) {
            val mHolder = holder as HomeProductViewHolder
            mHolder.label.text = context.getString(menu.productMenuLabel)
            Glide.with(mHolder.image)
                .load(ContextCompat.getDrawable(context, menu.productImageMenu))
                .into(mHolder.image)
        } else {
            val mHolder = holder as BottomsheetProductViewHolder
            mHolder.label.text = context.getString(menu.productMenuLabel)

            if (position == 0) {
                mHolder.main.updatePadding(
                    left = 0
                )
            }

            if (menu.isSelected) {
                mHolder.label.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.background_item_transaction_menu_label_selected
                )
                val typeFace = ResourcesCompat.getFont(
                    context,
                    com.fadlurahmanf.bebas_ui.R.font.lexend_deca_bold
                )
                mHolder.label.typeface = typeFace
                mHolder.label.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                mHolder.label.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.background_item_transaction_menu_label_unselected
                )
                val typeFace = ResourcesCompat.getFont(
                    context,
                    com.fadlurahmanf.bebas_ui.R.font.lexend_deca_regular
                )
                mHolder.label.typeface = typeFace
                mHolder.label.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }
        }

        holder.itemView.setOnClickListener {
            callback?.onTransactionMenuClicked(menu)
        }
    }

    interface Callback {
        fun onTransactionMenuClicked(menuModel: ProductTransactionMenuModel)
    }
}