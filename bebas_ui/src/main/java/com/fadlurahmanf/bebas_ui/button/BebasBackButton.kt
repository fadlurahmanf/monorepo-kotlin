package com.fadlurahmanf.bebas_ui.button

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasBackButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray

    private var iv: ImageView
    private var llMain: LinearLayout

    private var color: Int

    init {
        inflate(context, R.layout.cust_bebas_back_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasBackButton)

        iv = findViewById(R.id.iv_back)
        llMain = findViewById(R.id.ll_main)

        color = attributes.getColor(
            R.styleable.BebasBackButton_color,
            resources.getColor(R.color.black)
        )

        setup()
    }

    private fun setup() {
        iv.imageTintList = ColorStateList.valueOf(color)
        if (color == resources.getColor(R.color.white)) {
            iv.background =
                ContextCompat.getDrawable(this.context, R.drawable.white_circle_outlined)
        } else {
            iv.background =
                ContextCompat.getDrawable(this.context, R.drawable.black_circle_outlined)
        }

        llMain.setOnClickListener {
            (this.context as Activity).finish()
        }
    }
}