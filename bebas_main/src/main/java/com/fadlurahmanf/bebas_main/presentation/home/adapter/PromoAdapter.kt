package com.fadlurahmanf.bebas_main.presentation.home.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.core_ui.glide.GlideUrlCachedKey

class PromoAdapter : RecyclerView.Adapter<PromoAdapter.ViewHolder>() {
    private lateinit var context: Context

    private val promos: ArrayList<ItemPromoResponse> = arrayListOf()

    fun setList(promos: List<ItemPromoResponse>) {
        this.promos.clear()
        this.promos.addAll(promos)
        notifyItemRangeInserted(0, promos.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagePromo: ImageView = view.findViewById(R.id.iv_promo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = promos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val promo = promos[position]

        holder.imagePromo.setPadding(150)
        Glide.with(holder.imagePromo)
            .load(GlideUrlCachedKey(promo.imageOriginal ?: "", "${promo.id}_thumbnail"))
            .placeholder(ContextCompat.getDrawable(context, R.drawable.il_bebas_shimmer))
            .error(ContextCompat.getDrawable(context, R.drawable.il_bebas_shimmer))
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imagePromo.setPadding(150)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imagePromo.setPadding(0)
                    holder.imagePromo.background = null
                    return false
                }
            }).into(holder.imagePromo)
    }
}