package com.fadlurahmanf.core_mlkit.external

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

class CoreMlkitUtility {
    companion object {
        fun getBase64FromBitmap(bitmap: Bitmap): String? {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val newBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height / 2)
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }
    }
}