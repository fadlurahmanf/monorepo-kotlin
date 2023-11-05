package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_onboarding.R

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    private val list: ArrayList<WelcomeBannerResponse> = arrayListOf()

    fun setList(list: List<WelcomeBannerResponse>) {
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.tv_banner_description)
        val bannerImage: ImageView = view.findViewById(R.id.iv_banner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_welcome_banner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val banner = list[position]

        holder.description.text = banner.descriptionId
        Glide.with(holder.bannerImage).load(banner.image).into(holder.bannerImage)
    }
}