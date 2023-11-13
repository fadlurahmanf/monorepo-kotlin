package com.fadlurahmanf.bebas_ui.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasEdittext(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var editText: EditText
    private var label: TextView

    var hintInput: String?
    var labelInput: String?
    private var editTextHasFocus: Boolean = false

    var text: String
        get() = editText.text.toString()
        set(value) {
            editText.setText(value)
        }

    init {
        inflate(context, R.layout.cust_bebas_edittext, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasEdittext)

        editText = findViewById(R.id.et)
        label = findViewById(R.id.tv_label)

        hintInput = attributes.getString(R.styleable.BebasEdittext_hint)
        labelInput = attributes.getString(R.styleable.BebasEdittext_label)


        Log.d(
            "BebasLogger",
            "TES: ${attributes.getInt(R.styleable.BebasEdittext_android_imeOptions, 0)}"
        )
        Log.d("BebasLogger", "DEFAULT: ${editText.imeOptions}")

        editText.imeOptions = attributes.getInt(R.styleable.BebasEdittext_android_imeOptions, 0)

        setup()
    }

    private lateinit var textWatcher: TextWatcher

    private fun setup() {
        if (labelInput != null && hintInput != null) {
            label.text = labelInput ?: "-"
            editText.hint = hintInput ?: "-"
        } else {
            label.text = labelInput ?: hintInput ?: ""
            editText.hint = hintInput ?: labelInput ?: ""
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeEditTextStyle()
            }

            override fun afterTextChanged(s: Editable?) {}

        }

        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { v, hasFocus ->
            editTextHasFocus = hasFocus
            changeEditTextStyle()
        }
    }

    private fun changeEditTextStyle() {
        if (editTextHasFocus && editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
        } else if (editTextHasFocus && editTextLength() <= 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextHint)
        } else if (editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
        } else if (editTextLength() <= 0) {
            label.visibility = View.GONE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
        }
    }

    private fun editTextLength() = editText.length()


}