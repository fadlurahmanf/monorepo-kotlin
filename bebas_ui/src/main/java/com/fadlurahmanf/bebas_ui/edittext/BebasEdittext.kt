package com.fadlurahmanf.bebas_ui.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasEdittext(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var editText: EditText
    private var label: TextView
    private var llMain: LinearLayout
    private var drawableStart: ImageView

    var hintInput: String?
    var labelInput: String?
    private var editTextHasFocus: Boolean = false

    private var watcher: BebasEdittextTextWatcher? = null

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
        llMain = findViewById(R.id.ll_main)
        drawableStart = findViewById(R.id.drawable_start)

        hintInput = attributes.getString(R.styleable.BebasEdittext_hint)
        labelInput = attributes.getString(R.styleable.BebasEdittext_label)

        editText.imeOptions = attributes.getInt(R.styleable.BebasEdittext_android_imeOptions, 0)
        editText.inputType = attributes.getInt(R.styleable.BebasEdittext_android_inputType, 0)

        val drawable = attributes.getDrawable(R.styleable.BebasPhoneNumberEdittext_android_src)
        if (drawable != null) {
            drawableStart.visibility = View.VISIBLE
            drawableStart.setImageDrawable(drawable)
        }

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
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                watcher?.beforeTextChanged(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                watcher?.onTextChanged(s, start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                changeEditTextStyle()
                watcher?.afterTextChanged(s)
            }

        }

        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { v, hasFocus ->
            editTextHasFocus = hasFocus
            changeEditTextStyle()
        }
    }

    fun addTextChangedListener(watcher: BebasEdittextTextWatcher) {
        this.watcher = watcher
    }

    private fun changeEditTextStyle() {
        if (editTextHasFocus && editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextHasFocus && editTextLength() <= 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextHint)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        } else if (editTextLength() <= 0) {
            label.visibility = View.GONE
            editText.setTextAppearance(this.context, R.style.Font_Edittext)
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        }
    }

    private fun editTextLength() = editText.length()

    interface BebasEdittextTextWatcher {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?)
    }

}