package com.fadlurahmanf.bebas_transaction.external

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.LinearLayout
import com.fadlurahmanf.bebas_transaction.R

class BebasKeyboardTransaction(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private var attributes: TypedArray

    init {
        inflate(context, R.layout.layout_keyboard_transaction, this)
        attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.BebasKeyboardTransaction)


        setup()
    }

    private fun setup() {

    }
}