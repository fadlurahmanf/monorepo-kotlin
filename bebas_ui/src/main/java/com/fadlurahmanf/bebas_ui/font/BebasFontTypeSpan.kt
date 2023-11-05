package com.fadlurahmanf.bebas_ui.font

import android.content.res.AssetManager
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class BebasFontTypeSpan(family:String, private val typeface: Typeface) :
    TypefaceSpan(family) {

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = typeface
    }
}