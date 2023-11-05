package com.fadlurahmanf.bebas_ui.presentation.presentation.button

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasOutlinedButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var button: Button

    var text: String

    init {
        inflate(context, R.layout.cust_bebas_outlined_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasOutlinedButton)

        button = findViewById(R.id.btn)

        text = attributes.getString(R.styleable.BebasOutlinedButton_text) ?: ""

        setup()
    }

    private fun setup() {
        setButtonText(text)
    }

    fun setButtonText(text: String) {
        button.text = text
    }

    fun onClicked(l: View.OnClickListener?) {
        button.setOnClickListener(l)
    }
}