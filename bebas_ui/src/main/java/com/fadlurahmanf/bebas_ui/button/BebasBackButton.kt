package com.fadlurahmanf.bebas_ui.button

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.fadlurahmanf.bebas_ui.R

class BebasBackButton(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray

    private var iv: ImageView
    private var llMain: LinearLayout

    init {
        inflate(context, R.layout.cust_bebas_back_button, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasBackButton)

        iv = findViewById(R.id.iv_back)
        llMain = findViewById(R.id.ll_main)

        setup()
    }

    private fun setup() {
        llMain.setOnClickListener {
            (this.context as Activity).finish()
        }
    }
}