package com.fadlurahmanf.bebas_ui.button

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasRoundedButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var button: Button

    private var text: String
    private var active: Boolean

    init {
        inflate(context, R.layout.cust_bebas_rounded_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasRoundedButton)

        button = findViewById(R.id.btn)

        text = attributes.getString(R.styleable.BebasRoundedButton_text) ?: ""
        active = attributes.getBoolean(R.styleable.BebasRoundedButton_active, true)

        setup()
    }

    private fun setup() {
        setButtonText(text)
        setActive(active)
    }

    fun setActive(active: Boolean) {
        this.active = active
        if (active) {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_primary_button_background)
            button.setOnClickListener(listener)
            button.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
            )
        } else {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_grey_button_background)
            button.setOnClickListener(null)
            button.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
            )
        }
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    private var listener: OnClickListener? = null

    override fun setOnClickListener(l: OnClickListener?) {
        if (active) {
            listener = l
            button.setOnClickListener(l)
        }
    }
}