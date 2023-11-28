package com.fadlurahmanf.bebas_shared.validator

import android.util.Log
import com.fadlurahmanf.bebas_shared.R
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import java.util.Calendar
import java.util.concurrent.TimeUnit

object GeneralValidator {
    fun validateNik(nik: String): EditTextFormState<String> {
        val formattedNik = nik.replace(" ", "")
        if (formattedNik.isEmpty()) {
            return EditTextFormState.EMPTY
        }
        return if (formattedNik.length == 16) {
            EditTextFormState.SUCCESS(text = formattedNik)
        } else {
            EditTextFormState.FAILED(
                text = formattedNik,
                idRawStringRes = R.string.error_message_length_nik
            )
        }
    }

    fun validateFullName(name: String): EditTextFormState<String> {
        if (name.isEmpty()) {
            return EditTextFormState.EMPTY
        }
        return if (name.length >= 3) {
            EditTextFormState.SUCCESS(text = name)
        } else {
            EditTextFormState.FAILED(
                text = name,
                idRawStringRes = R.string.error_message_length_full_name
            )
        }
    }

    fun validateGeneral(value: String): EditTextFormState<String> {
        return if (value.isEmpty()) {
            EditTextFormState.EMPTY
        } else {
            EditTextFormState.SUCCESS(text = value)
        }
    }

    fun validateBirhDate(birthDate: Long): EditTextFormState<Long> {
        val calendar = Calendar.getInstance()
        val timeMillis = calendar.timeInMillis
        val diff = timeMillis - birthDate
        val diff17years = 365 * 17
        return if (TimeUnit.MILLISECONDS.toDays(diff) >= diff17years.toLong()) {
            EditTextFormState.SUCCESS(text = birthDate)
        } else {
            EditTextFormState.FAILED(
                text = birthDate,
                idRawStringRes = R.string.invalid_email_format
            )
        }
    }
}