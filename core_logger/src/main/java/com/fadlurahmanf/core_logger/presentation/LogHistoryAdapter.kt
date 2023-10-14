package com.fadlurahmanf.core_logger.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.core_logger.R
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class LogHistoryAdapter : RecyclerView.Adapter<LogHistoryAdapter.ViewHolder>() {
    private lateinit var context: Context
    private val logs = arrayListOf<LoggerEntity>()

    fun setLogs(logs: ArrayList<LoggerEntity>) {
        this.logs.clear()
        this.logs.addAll(logs)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.tv_log)
        val date: TextView = view.findViewById(R.id.tv_log_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = logs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = logs[position]
        holder.text.text = "[${log.type}][${log.tag}] - ${log.text}"
        holder.date.text = getFormattedDate(log.date)

        when (log.type) {
            "ERROR" -> {
                holder.text.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            "INFO" -> {
                holder.text.setTextColor(ContextCompat.getColor(context, R.color.blue))
            }
            else -> {
                holder.text.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    private fun getFormattedDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(Date(timestamp))
    }
}