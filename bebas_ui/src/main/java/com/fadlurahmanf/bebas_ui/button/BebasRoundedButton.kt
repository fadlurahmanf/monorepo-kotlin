package com.fadlurahmanf.bebas_ui.button

import android.content.Context
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

    var text: String
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

    private fun setActive(active: Boolean) {
        if (active) {
            button.background =
                ContextCompat.getDrawable(context, R.drawable.rounded_primary_button_background)
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