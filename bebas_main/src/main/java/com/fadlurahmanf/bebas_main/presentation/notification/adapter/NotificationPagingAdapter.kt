package com.fadlurahmanf.bebas_main.presentation.notification.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.data.dto.model.notification.NotificationModel

class NotificationPagingAdapter :
    PagingDataAdapter<NotificationModel, NotificationPagingAdapter.ViewHolder>(DiffUtilCallBack()) {
    private lateinit var context: Context
    private var callBack: CallBack? = null
    private var mapModel: HashMap<Int, NotificationModel> = hashMapOf()
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.tv_header)
        val body: TextView = view.findViewById(R.id.tv_body)
        val date: TextView = view.findViewById(R.id.tv_date)
        val mainLayout: LinearLayout = view.findViewById(R.id.ll_main)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = getItem(position) ?: mapModel[position]
        if (mapModel[position] == null && notification != null) {
            mapModel[position] = notification
        }

        holder.header.text = notification?.titleMessage ?: "-"
        holder.body.text = notification?.bodyMessage ?: "-"
        holder.date.text = notification?.time ?: "-"

        if (notification?.additionalData?.read == true) {
            holder.mainLayout.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.mainLayout.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.warm_red))
        }

        holder.itemView.setOnClickListener {
            if (notification != null) {
                callBack?.onClicked(notification)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationPagingAdapter.ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<NotificationModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationModel,
            newItem: NotificationModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationModel,
            newItem: NotificationModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface CallBack {
        fun onClicked(notification: NotificationModel)
    }
}