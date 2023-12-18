package com.fadlurahmanf.bebas_ui.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasMaskingEdittext(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var editText: EditText
    private var label: TextView
    private var llMain: LinearLayout
    private var drawableStart: ImageView
    private var errorTv: TextView

    var hintInput: String?
    var formatType: Int?
    var maxLengthIncludingMaskingChar: Int?
    var labelInput: String?
    var errorText: String? = null
    private var editTextHasFocus: Boolean = false
    private var fieldError: Boolean = false
    private var isTextAllCaps: Boolean = false

    private var watcher: BebasMaskingEdittextTextWatcher? = null

    var text: String
        get() = editText.text.toString()
        set(value) {
            editText.setText(value)
        }

    init {
        inflate(context, R.layout.cust_bebas_edittext, this)
        attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.BebasMaskingEdittext)

        editText = findViewById(R.id.et)
        label = findViewById(R.id.tv_label)
        llMain = findViewById(R.id.ll_main)
        drawableStart = findViewById(R.id.drawable_start)
        errorTv = findViewById(R.id.tv_error)

        hintInput = attributes.getString(R.styleable.BebasMaskingEdittext_hint)
        labelInput = attributes.getString(R.styleable.BebasMaskingEdittext_label)
        isTextAllCaps =
            attributes.getBoolean(R.styleable.BebasMaskingEdittext_android_textAllCaps, false)

        editText.imeOptions =
            attributes.getInt(R.styleable.BebasMaskingEdittext_android_imeOptions, 0)
        editText.inputType =
            attributes.getInt(R.styleable.BebasMaskingEdittext_android_inputType, 0)
        maxLengthIncludingMaskingChar =
            attributes.getInt(R.styleable.BebasMaskingEdittext_maxLengthIncludingMaskingChar, 0)
        formatType = attributes.getInt(R.styleable.BebasMaskingEdittext_formatType, 0)

        val drawable =
            attributes.getDrawable(R.styleable.BebasMaskingEdittext_android_drawableStart)
        if (drawable != null) {
            drawableStart.visibility = View.VISIBLE
            drawableStart.setImageDrawable(drawable)
        }

        setup()
    }

    private lateinit var textWatcher: TextWatcher

    private var isFormatting: Boolean = false

    private fun setup() {
        if (labelInput != null && hintInput != null) {
            label.text = labelInput ?: "-"
            editText.hint = hintInput ?: "-"
        } else {
            label.text = labelInput ?: hintInput ?: ""
            editText.hint = hintInput ?: labelInput ?: ""
        }

        // rtrw example: 007/021 -> t char with '/'
        if (formatType == 1) {
            val arraysFilter = arrayListOf<InputFilter>(InputFilter.LengthFilter(7))
            if (isTextAllCaps) {
                arraysFilter.add(InputFilter.AllCaps())
            }
            editText.filters = arraysFilter.toTypedArray()
        } else {
            if (maxLengthIncludingMaskingChar != null && (maxLengthIncludingMaskingChar ?: 0) > 0) {
                val arraysFilter =
                    arrayListOf<InputFilter>(InputFilter.LengthFilter(maxLengthIncludingMaskingChar!!))
                if (isTextAllCaps) {
                    arraysFilter.add(InputFilter.AllCaps())
                }
                editText.filters = arraysFilter.toTypedArray()
            }
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

                if (isFormatting) {
                    return
                }

                isFormatting = true

                // rtrw
                if (formatType == 1) {
                    // Remove any previous non-digit characters
                    val digits = s?.toString()?.replace("/".toRegex(), "") ?: ""

                    // Add spaces every 4 characters
                    val formattedNumber = StringBuilder()
                    for (i in digits.indices) {
                        if (i > 0 && i % 3 == 0) {
                            formattedNumber.append("/")
                        }
                        formattedNumber.append(digits[i])
                    }

                    editText.setText(formattedNumber.toString())

                    // Move the cursor to the end of the formatted text
                    editText.setSelection(formattedNumber.length)

                    isFormatting = false
                    watcher?.afterTextChanged(
                        s,
                        formattedNumber.toString(),
                        s?.toString()?.replace("/".toRegex(), "")
                    )
                } else {
                    // Remove any previous non-digit characters
                    val digits = s?.toString()?.replace("\\D".toRegex(), "") ?: ""

                    // Add spaces every 4 characters
                    val formattedNumber = StringBuilder()
                    for (i in digits.indices) {
                        if (i > 0 && i % 4 == 0) {
                            formattedNumber.append(" ")
                        }
                        formattedNumber.append(digits[i])
                    }

                    editText.setText(formattedNumber.toString())

                    // Move the cursor to the end of the formatted text
                    editText.setSelection(formattedNumber.length)

                    isFormatting = false
                    watcher?.afterTextChanged(
                        s,
                        formattedNumber.toString(),
                        s?.toString()?.replace("\\D".toRegex(), "")
                    )
                }
            }

        }

        editText.addTextChangedListener(textWatcher)
        editText.setOnFocusChangeListener { v, hasFocus ->
            editTextHasFocus = hasFocus
            changeEditTextStyle()
        }
    }

    fun addTextChangedListener(watcher: BebasMaskingEdittextTextWatcher) {
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
        fieldError = false
        errorText = null
        errorTv.visibility = View.GONE
        errorTv.text = ""
        changeEditTextStyle()
    }

    fun resetEmpty() {
        errorTv.visibility = View.GONE
    }

    private fun changeEditTextStyle() {
        if (editTextHasFocus && editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextHasFocus && editTextLength() <= 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2Hint)
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextLength() > 0) {
            label.visibility = View.VISIBLE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2)
            editText.background = ContextCompat.getDrawable(this.context, R.color.white)
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        } else if (editTextLength() <= 0) {
            label.visibility = View.GONE
            editText.setTextAppearance(this.context, R.style.Font_EdittextV2Hint)
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

    private fun editTextLength() = editText.length()


    interface BebasMaskingEdittextTextWatcher {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?, formattedText: String?, unformattedText: String?)
    }
}