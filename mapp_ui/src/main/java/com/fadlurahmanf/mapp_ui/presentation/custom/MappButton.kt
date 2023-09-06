package com.fadlurahmanf.mapp_ui.presentation.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.fadlurahmanf.mapp_ui.R

class MappButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var button: Button

    var text: String
    private var active: Boolean

    init {
        inflate(context, R.layout.cust_mapp_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.MappButton)

        button = findViewById(R.id.btn)

        text = attributes.getString(R.styleable.MappButton_text) ?: ""
        active = attributes.getBoolean(R.styleable.MappButton_active, true)

        setup()
    }

    private fun setup() {
        setButtonText(text)
        setActive(active)
    }

    private fun setActive(active: Boolean) {
        if (active) {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_green_button_background)
        } else {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_grey_button_background)
        }
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    fun onClicked(l: View.OnClickListener?) {
        button.setOnClickListener(l)
    }
}