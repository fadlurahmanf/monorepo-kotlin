package com.fadlurahmanf.bebas_transaction.external

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fadlurahmanf.bebas_transaction.R

class BebasKeyboardTransaction(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private var attributes: TypedArray

    private var tv1: TextView
    private var tv2: TextView
    private var tv3: TextView
    private var ivUnfocus: ImageView
    private var tv4: TextView
    private var tv5: TextView
    private var tv6: TextView
    private var tvClear: TextView
    private var tv7: TextView
    private var tv8: TextView
    private var tv9: TextView
    private var ivBackspace: ImageView
    private var tv0: TextView
    private var tv000: TextView
    private var tvDone: TextView

    init {
        inflate(context, R.layout.layout_keyboard_transaction, this)
        attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.BebasKeyboardTransaction)

        tv1 = findViewById(R.id.tv_1)
        tv2 = findViewById(R.id.tv_2)
        tv3 = findViewById(R.id.tv_3)

        ivUnfocus = findViewById(R.id.iv_unfocus)
        tv4 = findViewById(R.id.tv_4)
        tv5 = findViewById(R.id.tv_5)
        tv6 = findViewById(R.id.tv_6)
        tvClear = findViewById(R.id.tv_clear)
        tv7 = findViewById(R.id.tv_7)
        tv8 = findViewById(R.id.tv_8)
        tv9 = findViewById(R.id.tv_9)
        ivBackspace = findViewById(R.id.iv_backspace)
        tv0 = findViewById(R.id.tv_0)
        tv000 = findViewById(R.id.tv_000)
        tvDone = findViewById(R.id.tv_done)

        setup()
    }

    private fun setup() {
        tv1.setOnClickListener {
            callbackKeyboard.onDigitClicked("1")
        }

        tv2.setOnClickListener {
            callbackKeyboard.onDigitClicked("2")
        }

        tv3.setOnClickListener {
            callbackKeyboard.onDigitClicked("3")
        }

        tv4.setOnClickListener {
            callbackKeyboard.onDigitClicked("4")
        }

        tv5.setOnClickListener {
            callbackKeyboard.onDigitClicked("5")
        }

        tv6.setOnClickListener {
            callbackKeyboard.onDigitClicked("6")
        }

        tv7.setOnClickListener {
            callbackKeyboard.onDigitClicked("7")
        }

        tv8.setOnClickListener {
            callbackKeyboard.onDigitClicked("8")
        }

        tv9.setOnClickListener {
            callbackKeyboard.onDigitClicked("9")
        }

        tv0.setOnClickListener {
            callbackKeyboard.onDigitClicked("0")
        }

        tv000.setOnClickListener {
            callbackKeyboard.onDigitClicked("000")
        }

        ivUnfocus.setOnClickListener {
            callbackKeyboard.onFinish()
        }

        tvClear.setOnClickListener {
            callbackKeyboard.onClearClicked()
        }

        ivBackspace.setOnClickListener {
            callbackKeyboard.onBackspaceClicked()
        }

        tvDone.setOnClickListener {
            callbackKeyboard.onFinish()
        }
    }

    lateinit var callbackKeyboard: CallbackKeyboard
    fun setOnClickKeyboard(listener: CallbackKeyboard) {
        this.callbackKeyboard = listener
    }

    interface CallbackKeyboard {
        fun onClearClicked()
        fun onBackspaceClicked()
        fun onDigitClicked(digit: String)
        fun onFinish()
    }
}