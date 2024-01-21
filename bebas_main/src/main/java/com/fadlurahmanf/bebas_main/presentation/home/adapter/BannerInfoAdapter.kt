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
import com.fadlurahmanf.bebas_api.data.dto.home.HomePageBannerInfoResponse
import com.fadlurahmanf.bebas_main.R

class BannerInfoAdapter : RecyclerView.Adapter<BannerInfoAdapter.ViewHolder>() {
    private lateinit var context: Context

    private val bannerInfos: ArrayList<HomePageBannerInfoResponse> = arrayListOf()

    fun setList(bannerInfos: List<HomePageBannerInfoResponse>) {
        this.bannerInfos.clear()
        this.bannerInfos.addAll(bannerInfos)
        notifyItemRangeInserted(0, bannerInfos.size)
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

    override fun getItemCount(): Int = bannerInfos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bannerInfo = bannerInfos[position]

        holder.imagePromo.setPadding(150)
        Glide.with(holder.imagePromo)
            .load(bannerInfo.thumbnailImage ?: "")
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