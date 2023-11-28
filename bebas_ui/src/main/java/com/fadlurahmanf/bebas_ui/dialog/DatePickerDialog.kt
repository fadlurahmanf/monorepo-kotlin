package com.fadlurahmanf.bebas_ui.dialog

import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import com.fadlurahmanf.bebas_ui.databinding.DialogDatePickerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatePickerDialog(
    val date: String? = null
) :
    BaseDialog<DialogDatePickerBinding>(
        DialogDatePickerBinding::inflate
    ) {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    var birthDate: Date? = null

    private val onDateChangedListener =
        OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            callback?.onClicked(year, monthOfYear, dayOfMonth, calendar.time)
        }

    override fun setup() {

        try {
            if (date != null) {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                birthDate = sdf.parse(date)

                val calendar = Calendar.getInstance()
                birthDate?.let {
                    calendar.time = birthDate!!
                }

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                binding.dp.init(
                    year,
                    month,
                    dayOfMonth,
                    onDateChangedListener
                )
            }
        } catch (e: Exception) {
            birthDate = null
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.dp.setOnDateChangedListener(onDateChangedListener)
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