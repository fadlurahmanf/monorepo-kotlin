package com.fadlurahmanf.bebas_ui.button

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasOutlinedButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray
    private var buttonMainLayout: LinearLayout
    private var buttonText: TextView
    private var drawableStart: ImageView

    var text: String

    init {
        inflate(context, R.layout.cust_bebas_outlined_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasOutlinedButton)

        buttonMainLayout = findViewById(R.id.ll_main)
        buttonText = findViewById(R.id.tv_btn_text)
        drawableStart = findViewById(R.id.drawable_start)

        text = attributes.getString(R.styleable.BebasOutlinedButton_text) ?: ""

        val p0Drawable =
            attributes.getDrawable(R.styleable.BebasOutlinedButton_android_drawableStart)

        if (p0Drawable != null) {
            drawableStart.visibility = View.VISIBLE
            drawableStart.setImageDrawable(p0Drawable)
        }

        setup()
    }

    private fun setup() {
        setButtonText(text)
    }

    fun setButtonText(text: String) {
        buttonText.text = text
    }

    override fun setOnClickListener(l: OnClickListener?) {
        buttonMainLayout.setOnClickListener(l)
    }
}