package com.fadlurahmanf.bebas_ui.dialog

import android.os.Build
import com.fadlurahmanf.bebas_ui.databinding.DialogDatePickerBinding
import java.util.Calendar
import java.util.Date

class DatePickerDialog :
    BaseDialog<DialogDatePickerBinding>(
        DialogDatePickerBinding::inflate
    ) {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun setup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.dp.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                callback?.onClicked(year, monthOfYear, dayOfMonth, calendar.time)
            }
        } else {
            binding.dp.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                callback?.onClicked(year, month, dayOfMonth, calendar.time)
            }
        }
    }

    interface Callback {
        fun onClicked(year: Int, month: Int, dayOfMonth: Int, date: Date)
    }

}