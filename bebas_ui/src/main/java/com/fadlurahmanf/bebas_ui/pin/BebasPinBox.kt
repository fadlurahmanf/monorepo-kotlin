package com.fadlurahmanf.bebas_ui.pin

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasPinBox(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray

    private var pin1: View
    private var pin2: View
    private var pin3: View
    private var pin4: View
    private var pin5: View
    private var pin6: View

    var pin: String = ""
        get() = field
        set(value) {
            field = value
            setPinBoxFilled(value)
        }

    private fun setPinBoxFilled(value: String) {
        when (value.length) {
            6 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin3.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin4.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin5.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin6.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
            }

            5 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin3.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin4.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin5.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }

            4 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin3.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin4.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin5.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }

            3 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin3.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin4.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin5.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }

            2 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin3.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin4.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin5.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }

            1 -> {
                pin1.background =
                    ContextCompat.getDrawable(context, R.drawable.background_pin_filled)
                pin2.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin3.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin4.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin5.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }

            else -> {
                pin1.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin2.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin3.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin4.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin5.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
                pin6.background = ContextCompat.getDrawable(context, R.drawable.background_pin)
            }
        }
    }

    init {
        inflate(context, R.layout.layout_pin_box, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasPinBox)

        pin1 = findViewById(R.id.v_pin_1)
        pin2 = findViewById(R.id.v_pin_2)
        pin3 = findViewById(R.id.v_pin_3)
        pin4 = findViewById(R.id.v_pin_4)
        pin5 = findViewById(R.id.v_pin_5)
        pin6 = findViewById(R.id.v_pin_6)
    }

    private fun setup() {}
}