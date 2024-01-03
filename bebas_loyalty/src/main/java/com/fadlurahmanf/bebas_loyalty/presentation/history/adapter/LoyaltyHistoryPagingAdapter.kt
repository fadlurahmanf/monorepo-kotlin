package com.fadlurahmanf.bebas_loyalty.presentation.history.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.bebas_loyalty.R
import com.fadlurahmanf.bebas_loyalty.data.dto.HistoryLoyaltyModel
import com.fadlurahmanf.bebas_shared.extension.formatHeaderHistoryLoyalty
import com.fadlurahmanf.bebas_shared.extension.formatLabelHistoryLoyaltyDate
import com.fadlurahmanf.bebas_shared.extension.utcToLocal

class LoyaltyHistoryPagingAdapter :
    PagingDataAdapter<HistoryLoyaltyModel, RecyclerView.ViewHolder>(DiffUtilCallBack()) {
    private lateinit var context: Context
    private var callBack: CallBack? = null
    private var mapModel: HashMap<Int, HistoryLoyaltyModel> = hashMapOf()
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: TextView = view.findViewById(R.id.tv_header)
    }

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagIcon: ImageView = view.findViewById(R.id.iv_flag)
        val fromAccount: TextView = view.findViewById(R.id.tv_from_account)
        val body: TextView = view.findViewById(R.id.tv_body_message_history)
        val date: TextView = view.findViewById(R.id.tv_date)
        val bebasPoint: TextView = view.findViewById(R.id.tv_bebaspoin)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position) ?: mapModel[position]
        if (mapModel[position] == null && model != null) {
            mapModel[position] = model
        }

        if (getItemViewType(position) == 0) {
            val mHolder = holder as HeaderViewHolder
            mHolder.header.text = model?.header ?: "-"
        } else {
            val mHolder = holder as ContentViewHolder

            if (model?.response?.entryType == "expiry") {
                mHolder.fromAccount.visibility = View.GONE
                Glide.with(mHolder.flagIcon)
                    .load(ContextCompat.getDrawable(context, R.drawable.round_remove_24))
                    .into(mHolder.flagIcon)
                mHolder.flagIcon.imageTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.red))

                mHolder.bebasPoint.text = "- ${model?.response?.point ?: "-"}"
                mHolder.bebasPoint.setTextColor(context.resources.getColor(R.color.red))
            } else if (model?.response?.entryType == "redemption") {
                mHolder.fromAccount.visibility = View.GONE
                Glide.with(mHolder.flagIcon)
                    .load(ContextCompat.getDrawable(context, R.drawable.round_remove_24))
                    .into(mHolder.flagIcon)
                mHolder.flagIcon.imageTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.red))

                mHolder.bebasPoint.text = "- ${model?.response?.point ?: "-"}"
                mHolder.bebasPoint.setTextColor(context.resources.getColor(R.color.red))
            } else {
                mHolder.fromAccount.visibility = View.VISIBLE
                mHolder.fromAccount.text = model?.topLabel ?: "-"

                Glide.with(mHolder.flagIcon)
                    .load(ContextCompat.getDrawable(context, R.drawable.round_add_24))
                    .into(mHolder.flagIcon)
                mHolder.flagIcon.imageTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.green))

                mHolder.bebasPoint.text = "+ ${model?.response?.point ?: "-"}"
                mHolder.bebasPoint.setTextColor(context.resources.getColor(R.color.green))
            }

            mHolder.body.text = model?.subLabel ?: "-"
            mHolder.date.text =
                model?.response?.createdDate?.utcToLocal()?.formatLabelHistoryLoyaltyDate()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }
        return if (viewType == 0) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header_history_loyalty, parent, false)
            HeaderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_history_loyalty, parent, false)
            ContentViewHolder(view)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<HistoryLoyaltyModel>() {
        override fun areItemsTheSame(
            oldItem: HistoryLoyaltyModel,
            newItem: HistoryLoyaltyModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HistoryLoyaltyModel,
            newItem: HistoryLoyaltyModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface CallBack {
        fun onClicked(notification: HistoryLoyaltyModel)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.type ?: mapModel[position]?.type ?: 1
    }
}