package com.fadlurahmanf.bebas_shared.validator

import com.fadlurahmanf.bebas_shared.state.EditTextFormState

object PhoneValidator {
    fun validatePhone(phone: String): EditTextFormState<String> {
        return if (phone.isEmpty()) {
            EditTextFormState.EMPTY
        } else if (phone.startsWith("62") || phone.startsWith("08")) {
            if (phone.length in 8..13) {
                EditTextFormState.SUCCESS(text = phone)
            } else {
                EditTextFormState.FAILED(text = phone, errorMessage = "PANJANG ${phone.length}")
            }
        } else {
            EditTextFormState.FAILED(text = phone, errorMessage = "FORMAT PHONE")
        }
    }
}