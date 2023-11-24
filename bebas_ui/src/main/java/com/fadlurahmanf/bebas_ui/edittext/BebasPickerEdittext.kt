package com.fadlurahmanf.bebas_ui.edittext

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasPickerEdittext(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var tvAnswer: TextView
    private var tvHint: TextView
    private var label: TextView
    private var llMain: LinearLayout
    private var drawableStart: ImageView
    private var drawableEnd: ImageView
    private var errorTv: TextView

    var hintInput: String?
    var labelInput: String?
    var errorText: String? = null
    private var editTextHasFocus: Boolean = false
    private var fieldError: Boolean = false

    private var isEnabled: Boolean = true


    private var watcher: BebasPickerEdittextTextWatcher? = null


    fun setText(text: String) {
        tvAnswer.text = text
        changeEditTextStyle()
    }

    init {
        inflate(context, R.layout.cust_bebas_picker_edittext, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasPickerEdittext)

        tvAnswer = findViewById(R.id.tv_answer)
        tvHint = findViewById(R.id.tv_hint)
        label = findViewById(R.id.tv_label)
        llMain = findViewById(R.id.ll_main)
        drawableStart = findViewById(R.id.drawable_start)
        drawableEnd = findViewById(R.id.drawable_end)
        errorTv = findViewById(R.id.tv_error)

        hintInput = attributes.getString(R.styleable.BebasPickerEdittext_hint)
        labelInput = attributes.getString(R.styleable.BebasPickerEdittext_label)


        val inputDrawableStart =
            attributes.getDrawable(R.styleable.BebasPickerEdittext_android_drawableStart)
        if (inputDrawableStart != null) {
            drawableStart.visibility = View.VISIBLE
            drawableStart.setImageDrawable(inputDrawableStart)
        }

        val inputDrawableEnd =
            attributes.getDrawable(R.styleable.BebasPickerEdittext_android_drawableEnd)
        if (inputDrawableEnd != null) {
            drawableEnd.visibility = View.VISIBLE
            drawableEnd.setImageDrawable(inputDrawableEnd)
        }


        setup()
    }

    private fun setup() {
        if (labelInput != null && hintInput != null) {
            label.text = labelInput ?: "-"
            tvHint.text = hintInput ?: ""
        } else {
            label.text = labelInput ?: hintInput ?: "-"
            tvHint.text = hintInput ?: labelInput ?: ""
        }

    }

    fun addTextChangedListener(watcher: BebasPickerEdittextTextWatcher) {
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
        tvAnswer.background = ContextCompat.getDrawable(this.context, R.color.white)
        tvHint.background = ContextCompat.getDrawable(this.context, R.color.white)

        if (editTextHasFocus && answerTextLength() > 0) {
            label.visibility = View.VISIBLE
            tvAnswer.visibility = View.VISIBLE
            tvHint.visibility = View.GONE
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (editTextHasFocus && answerTextLength() <= 0) {
            label.visibility = View.VISIBLE
            tvAnswer.visibility = View.GONE
            tvHint.visibility = View.VISIBLE
            llMain.background = ContextCompat.getDrawable(this.context, R.drawable.edittext_focused)
        } else if (answerTextLength() > 0) {
            label.visibility = View.VISIBLE
            tvAnswer.visibility = View.VISIBLE
            tvHint.visibility = View.GONE
            llMain.background =
                ContextCompat.getDrawable(this.context, R.drawable.edittext_unfocused)
        } else if (answerTextLength() <= 0) {
            label.visibility = View.GONE
            tvAnswer.visibility = View.GONE
            tvHint.visibility = View.VISIBLE
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
        tvAnswer.background = ContextCompat.getDrawable(this.context, R.color.warm_red)
        tvHint.background = ContextCompat.getDrawable(this.context, R.color.warm_red)
    }

    private fun answerTextLength() = tvAnswer.text.length

    // todo
//    fun setIsEnabled(enable: Boolean) {
//        this.isEnabled = enable
//        editText.isEnabled = enable
//        if (!enable) {
//            llMain.background =
//                ContextCompat.getDrawable(this.context, R.drawable.edittext_disabled)
//            editText.background = ContextCompat.getDrawable(this.context, R.color.light_grey_20)
//            label.setTextAppearance(this.context, R.style.Font_EdittextLabel_Disabled)
//            editText.setTextAppearance(this.context, R.style.Font_Edittext_Disabled)
//            drawableStart.imageTintList =
//                ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.black_opacity))
//        } else {
//            changeEditTextStyle()
//        }
//    }

    interface BebasPickerEdittextTextWatcher {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        llMain.setOnClickListener(l)
    }

}