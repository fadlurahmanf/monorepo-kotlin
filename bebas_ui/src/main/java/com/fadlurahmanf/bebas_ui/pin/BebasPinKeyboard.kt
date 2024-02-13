package com.fadlurahmanf.bebas_ui.pin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_ui.R

class BebasPinKeyboard(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {

    private var attributes: TypedArray

    private var tv1: TextView
    private var tv2: TextView
    private var tv3: TextView
    private var tv4: TextView
    private var tv5: TextView
    private var tv6: TextView
    private var tv7: TextView
    private var tv8: TextView
    private var tv9: TextView
    private var tv0: TextView
    private var tvForgotPin: TextView
    private var ivDelete: ImageView

    private var isTv1Clicked: Boolean = false
    private var isTv2Clicked: Boolean = false
    private var isTv3Clicked: Boolean = false
    private var isTv4Clicked: Boolean = false
    private var isTv5Clicked: Boolean = false
    private var isTv6Clicked: Boolean = false
    private var isTv7Clicked: Boolean = false
    private var isTv8Clicked: Boolean = false
    private var isTv9Clicked: Boolean = false
    private var isTv0Clicked: Boolean = false
    private var isForgotPinClicked: Boolean = false
    private var isDeletePinClicked: Boolean = false

    private var callback: Callback? = null

    private var isForgotPinVisible: Boolean = false

    init {
        inflate(context, R.layout.layout_pin_keyboard, this)
        attributes = context.obtainStyledAttributes(attributeSet, R.styleable.BebasPinKeyboard)

        isForgotPinVisible =
            attributes.getBoolean(R.styleable.BebasPinKeyboard_isForgotPasswordVisible, false)

        tv1 = findViewById(R.id.tv_1)
        tv2 = findViewById(R.id.tv_2)
        tv3 = findViewById(R.id.tv_3)
        tv4 = findViewById(R.id.tv_4)
        tv5 = findViewById(R.id.tv_5)
        tv6 = findViewById(R.id.tv_6)
        tv7 = findViewById(R.id.tv_7)
        tv8 = findViewById(R.id.tv_8)
        tv9 = findViewById(R.id.tv_9)
        tv0 = findViewById(R.id.tv_0)
        tvForgotPin = findViewById(R.id.tv_forgot_pin)
        ivDelete = findViewById(R.id.iv_delete)

        setup()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setup() {
        tv1.setOnClickListener {
            callback?.onPinClicked("1")
        }

        tv1.setOnTouchListener { v, event ->
            if (!isTv1Clicked && (event.action == 0 || event.action == 2)) {
                isTv1Clicked = true
                updatePinIsClickedOrNot(tv1, true)
                return@setOnTouchListener false
            } else if (isTv1Clicked && event.action == 1) {
                isTv1Clicked = false
                updatePinIsClickedOrNot(tv1, false)
                return@setOnTouchListener false
            }
            false
        }

        tv2.setOnClickListener {
            callback?.onPinClicked("2")
        }

        tv2.setOnTouchListener { v, event ->
            if (!isTv2Clicked && (event.action == 0 || event.action == 2)) {
                isTv2Clicked = true
                updatePinIsClickedOrNot(tv2, true)
                return@setOnTouchListener false
            } else if (isTv2Clicked && event.action == 1) {
                isTv2Clicked = false
                updatePinIsClickedOrNot(tv2, false)
                return@setOnTouchListener false
            }
            false
        }

        tv3.setOnClickListener {
            callback?.onPinClicked("3")
        }

        tv3.setOnTouchListener { v, event ->
            if (!isTv3Clicked && (event.action == 0 || event.action == 2)) {
                isTv3Clicked = true
                updatePinIsClickedOrNot(tv3, true)
                return@setOnTouchListener false
            } else if (isTv3Clicked && event.action == 1) {
                isTv3Clicked = false
                updatePinIsClickedOrNot(tv3, false)
                return@setOnTouchListener false
            }
            false
        }

        tv4.setOnClickListener {
            callback?.onPinClicked("4")
        }

        tv4.setOnTouchListener { v, event ->
            if (!isTv4Clicked && (event.action == 0 || event.action == 2)) {
                isTv4Clicked = true
                updatePinIsClickedOrNot(tv4, true)
                return@setOnTouchListener false
            } else if (isTv4Clicked && event.action == 1) {
                isTv4Clicked = false
                updatePinIsClickedOrNot(tv4, false)
                return@setOnTouchListener false
            }
            false
        }

        tv5.setOnClickListener {
            callback?.onPinClicked("5")
        }

        tv5.setOnTouchListener { v, event ->
            if (!isTv5Clicked && (event.action == 0 || event.action == 2)) {
                isTv5Clicked = true
                updatePinIsClickedOrNot(tv5, true)
                return@setOnTouchListener false
            } else if (isTv5Clicked && event.action == 1) {
                isTv5Clicked = false
                updatePinIsClickedOrNot(tv5, false)
                return@setOnTouchListener false
            }
            false
        }

        tv6.setOnClickListener {
            callback?.onPinClicked("6")
        }

        tv6.setOnTouchListener { v, event ->
            if (!isTv6Clicked && (event.action == 0 || event.action == 2)) {
                isTv6Clicked = true
                updatePinIsClickedOrNot(tv6, true)
                return@setOnTouchListener false
            } else if (isTv6Clicked && event.action == 1) {
                isTv6Clicked = false
                updatePinIsClickedOrNot(tv6, false)
                return@setOnTouchListener false
            }
            false
        }

        tv7.setOnClickListener {
            callback?.onPinClicked("7")
        }

        tv7.setOnTouchListener { v, event ->
            if (!isTv7Clicked && (event.action == 0 || event.action == 2)) {
                isTv7Clicked = true
                updatePinIsClickedOrNot(tv7, true)
                return@setOnTouchListener false
            } else if (isTv7Clicked && event.action == 1) {
                isTv7Clicked = false
                updatePinIsClickedOrNot(tv7, false)
                return@setOnTouchListener false
            }
            false
        }

        tv8.setOnClickListener {
            callback?.onPinClicked("8")
        }

        tv8.setOnTouchListener { v, event ->
            if (!isTv8Clicked && (event.action == 0 || event.action == 2)) {
                isTv8Clicked = true
                updatePinIsClickedOrNot(tv8, true)
                return@setOnTouchListener false
            } else if (isTv8Clicked && event.action == 1) {
                isTv8Clicked = false
                updatePinIsClickedOrNot(tv8, false)
                return@setOnTouchListener false
            }
            false
        }

        tv9.setOnClickListener {
            callback?.onPinClicked("9")
        }

        tv9.setOnTouchListener { v, event ->
            if (!isTv9Clicked && (event.action == 0 || event.action == 2)) {
                isTv9Clicked = true
                updatePinIsClickedOrNot(tv9, true)
                return@setOnTouchListener false
            } else if (isTv9Clicked && event.action == 1) {
                isTv9Clicked = false
                updatePinIsClickedOrNot(tv9, false)
                return@setOnTouchListener false
            }
            false
        }

        tv0.setOnClickListener {
            callback?.onPinClicked("0")
        }

        tv0.setOnTouchListener { v, event ->
            if (!isTv0Clicked && (event.action == 0 || event.action == 2)) {
                isTv0Clicked = true
                updatePinIsClickedOrNot(tv0, true)
                return@setOnTouchListener false
            } else if (isTv0Clicked && event.action == 1) {
                isTv0Clicked = false
                updatePinIsClickedOrNot(tv0, false)
                return@setOnTouchListener false
            }
            false
        }

        tvForgotPin.setOnClickListener {
            callback?.onForgotPinClicked()
        }

        tvForgotPin.setOnTouchListener { v, event ->
            if (!isForgotPinClicked && (event.action == 0 || event.action == 2)) {
                isForgotPinClicked = true
                updatePinIsClickedOrNot(tvForgotPin, true)
                return@setOnTouchListener false
            } else if (isForgotPinClicked && event.action == 1) {
                isForgotPinClicked = false
                updatePinIsClickedOrNot(tvForgotPin, false)
                return@setOnTouchListener false
            }
            false
        }

        ivDelete.setOnClickListener {
            callback?.onDeletePinClicked()
        }

        ivDelete.setOnTouchListener { v, event ->
            if (!isDeletePinClicked && (event.action == 0 || event.action == 2)) {
                isDeletePinClicked = true
                updateDeletePinIsClickedOrNot(ivDelete, true)
                return@setOnTouchListener false
            } else if (isDeletePinClicked && event.action == 1) {
                isDeletePinClicked = false
                updateDeletePinIsClickedOrNot(ivDelete, false)
                return@setOnTouchListener false
            }
            false
        }

        setForgotPin(isForgotPinVisible)
    }

    private fun setForgotPin(isVisible: Boolean) {
        isForgotPinVisible = isVisible
        if (isForgotPinVisible) {
            tvForgotPin.visibility = View.VISIBLE
        } else {
            tvForgotPin.visibility = View.INVISIBLE
        }
    }

    private fun updatePinIsClickedOrNot(textView: TextView, isClicked: Boolean) {
        if (isClicked) {
            textView.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_text_pin_filled)
                setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                )
            }
        } else {
            textView.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_text_pin)
                setTextColor(
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.red
                        )
                    )
                )
            }
        }
    }

    private fun updateDeletePinIsClickedOrNot(textView: ImageView, isClicked: Boolean) {
        if (isClicked) {
            textView.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_circle_black)
                imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
            }
        } else {
            textView.apply {
                background =
                    ContextCompat.getDrawable(context, R.drawable.background_circle_border_black)
                imageTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
            }
        }
    }

    interface Callback {
        fun onPinClicked(pin: String)
        fun onForgotPinClicked()
        fun onDeletePinClicked()
    }
}