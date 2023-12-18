package com.fadlurahmanf.bebas_transaction.presentation.favorite.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    lateinit var context: Context
    private var favorites: ArrayList<FavoriteContactModel> = arrayListOf()
    private var callback: Callback? = null

    fun setList(list: List<FavoriteContactModel>) {
        favorites.clear()
        favorites.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun insertModel(favorite: FavoriteContactModel) {
        favorites.add(favorite)
        notifyItemInserted(favorites.size)
    }

    fun removeModel(indexRemoved: Int) {
        favorites.removeAt(indexRemoved)
        notifyItemRemoved(indexRemoved)
    }

    fun changeFavoriteModel(list: List<FavoriteContactModel>, indexChanged: Int) {
        this.favorites = ArrayList(list)
        notifyItemChanged(indexChanged)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val initialAvatar = view.findViewById<TextView>(R.id.initialAvatar)
        val favoriteName = view.findViewById<TextView>(R.id.tv_favorite_name)
        val subFavoriteLavel = view.findViewById<TextView>(R.id.tv_sub_favorite)
        val pin = view.findViewById<ImageView>(R.id.iv_pin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorites[position]

        holder.favoriteName.text = favorite.nameInFavoriteContact
        if (favorite.nameInFavoriteContact.isNotEmpty()) {
            if (favorite.nameInFavoriteContact.contains(" ")) {
                val first = favorite.nameInFavoriteContact.split(" ").first().take(1)
                val second = favorite.nameInFavoriteContact.split(" ")[1].take(1)

                holder.initialAvatar.text = "$first$second"
            } else {
                holder.initialAvatar.text = favorite.nameInFavoriteContact.take(1)
            }
        }

        holder.subFavoriteLavel.text = "${favorite.labelTypeOfFavorite} • ${favorite.accountNumber}"

        if (favorite.isPinned) {
            holder.pin.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.round_push_pin_24
                )
            )
            holder.pin.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
        } else {
            holder.pin.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.outline_push_pin_24
                )
            )
            holder.pin.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.light_grey))
        }

        holder.pin.setOnClickListener {
            callback?.onPinClicked(favorite.isPinned, favorite)
        }

        holder.itemView.setOnClickListener {
            callback?.onItemClicked(favorite)
        }
    }

    interface Callback {
        fun onPinClicked(isCurrentPinned: Boolean, favorite: FavoriteContactModel)

        fun onItemClicked(favorite: FavoriteContactModel)
    }

}
