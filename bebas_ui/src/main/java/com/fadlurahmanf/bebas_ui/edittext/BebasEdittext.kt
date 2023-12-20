package com.fadlurahmanf.bebas_ui.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
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
    private var errorTv: TextView

    var hintInput: String?
    private var labelInput: String?

    fun setLabel(label: String) {
        this.label.text = label
    }

    var errorText: String? = null
    private var editTextHasFocus: Boolean = false
    private var fieldError: Boolean = false
    private var isTextAllCaps: Boolean = false

    private var isEnabled: Boolean = true


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
        errorTv = findViewById(R.id.tv_error)

        hintInput = attributes.getString(R.styleable.BebasEdittext_hint)
        labelInput = attributes.getString(R.styleable.BebasEdittext_label)
        isTextAllCaps = attributes.getBoolean(R.styleable.BebasEdittext_android_textAllCaps, false)

        editText.imeOptions = attributes.getInt(R.styleable.BebasEdittext_android_imeOptions, 0)
        editText.inputType = attributes.getInt(R.styleable.BebasEdittext_android_inputType, 0)

        val drawable = attributes.getDrawable(R.styleable.BebasEdittext_android_drawableStart)
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

        if (isTextAllCaps) {
            val arraysFilter = arrayListOf<InputFilter>()
            arraysFilter.add(InputFilter.AllCaps())
            editText.filters = arraysFilter.toTypedArray()
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
            if (hasFocus) {
                editText.setSelection(editText.text.length)
            }
            changeEditTextStyle()
        }
    }

    fun addTextChangedListener(watcher: BebasEdittextTextWatcher) {
        this.watcher = watcher
    }

    fun setError(error: String, fieldError: Boolean = false) {
        errorText = error
        this.fieldError = fieldError
        errorTv.visibility = View.VISIBLE
        errorTv.text = errorText
        changeEditTextStyle()
    }

    fun removeError() {
        errorText = null
        fieldError = false
        errorTv.visibility = View.GONE
        errorTv.text = ""
        changeEditTextStyle()
    }

    private fun changeEditTextStyle() {
        if (editTextHasFocus && editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.setHintTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            )
            label.setTextAppearance(this.context, R.style.Font_EdittextLabel)
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextHasFocus && editTextLength() <= 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.setHintTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            )
            label.setTextAppearance(this.context, R.style.Font_EdittextLabel)
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.setHintTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            )
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        } else if (editTextLength() <= 0) {
            label.visibility = View.GONE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.setHintTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            )
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        }

        if (fieldError) {
            changeFieldToError()
        }
    }

    private fun changeFieldToError() {
        llMain.background =
            ContextCompat.getDrawable(this.context, R.drawable.edittext_field_error)
        editText.background = ContextCompat.getDrawable(this.context, R.color.warm_red)
    }

    private fun editTextLength() = editText.text.length

    fun setIsEnabled(enable: Boolean) {
        this.isEnabled = enable
        editText.isEnabled = enable
        if (!enable) {
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_disabled)
            editText.background = ContextCompat.getDrawable(this.context, R.color.light_grey_20)
            label.setTextAppearance(this.context, R.style.Font_EdittextLabel_Disabled)
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2_Disabled)
            drawableStart.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.black_opacity))
        } else {
            changeEditTextStyle()
        }
    }

    interface BebasEdittextTextWatcher {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?)
    }

}