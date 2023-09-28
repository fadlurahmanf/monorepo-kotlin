package com.fadlurahmanf.mapp_analytic.external.helper

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object AnalyticHelper {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun logEvent(event:String, map:Map<String, Any>){
        firebaseAnalytics.logEvent(event, mapToBundle(map))
        Log.d("MappLogger", "SUCCESS TRACK $event with $map")
    }

    private fun mapToBundle(map:Map<String, Any>): Bundle {
        val bundle = Bundle()
        map.keys.forEach {
            when (val v = map[it]) {
                is String -> {
                    bundle.putString(it, v)
                }
                is Int -> {
                    bundle.putInt(it, v)
                }
                is Double -> {
                    bundle.putDouble(it, v)
                }
            }
        }
        return bundle
    }

}