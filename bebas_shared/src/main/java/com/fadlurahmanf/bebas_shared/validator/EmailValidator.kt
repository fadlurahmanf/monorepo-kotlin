package com.fadlurahmanf.bebas_shared.validator

import com.fadlurahmanf.bebas_shared.R
import com.fadlurahmanf.bebas_shared.state.EditTextFormState

object EmailValidator {
    fun validateEmail(email: String): EditTextFormState<String> {
        if (email.isEmpty()) {
            return EditTextFormState.EMPTY
        }
        val isEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return if (isEmail) {
            EditTextFormState.SUCCESS(text = email)
        } else {
            EditTextFormState.FAILED(
                text = email,
                idRawStringRes = R.string.invalid_email_format
            )
        }
    }
}