package com.fadlurahmanf.core_ui.glide

import android.util.Log
import com.bumptech.glide.load.model.GlideUrl

class GlideUrlCachedKey(url: String, private val coreCacheKey: String) : GlideUrl(url) {
    override fun getCacheKey(): String {
        return coreCacheKey
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = super.getHeaders()
        headers.keys.forEach {
            Log.d("CoreLogger", "HEADER GLIDE: $it: ${headers[it]}")
        }
        return headers
    }
}