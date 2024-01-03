package com.fadlurahmanf.bebas_loyalty.presentation.history.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_loyalty.R
import com.fadlurahmanf.bebas_loyalty.data.dto.HistoryLoyaltyModel

class LoyaltyHistoryPagingAdapter :
    PagingDataAdapter<HistoryLoyaltyModel, LoyaltyHistoryPagingAdapter.ViewHolder>(DiffUtilCallBack()) {
    private lateinit var context: Context
    private var callBack: CallBack? = null
    private var mapModel: HashMap<Int, HistoryLoyaltyModel> = hashMapOf()
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fromAccount: TextView = view.findViewById(R.id.tv_from_account)
        val body: TextView = view.findViewById(R.id.tv_body_message_history)
        val bebasPoint: TextView = view.findViewById(R.id.tv_bebaspoin)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position) ?: mapModel[position]
        if (mapModel[position] == null && model != null) {
            mapModel[position] = model
        }
        holder.fromAccount.text = model?.header ?: "-"
        holder.body.text = model?.body ?: "-"
        holder.bebasPoint.text =
            if ((model?.point ?: 0) > 0) "+ ${model?.point ?: "-"}" else "- ${model?.point ?: "-"}"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history_loyalty, parent, false)
        return ViewHolder(view)
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
}