package com.fadlurahmanf.bebas_transaction.external

import android.util.Log
import java.lang.Exception

class BebasTransactionHelper {
    companion object {
        fun getInitial(name: String): String {
            return try {
                if (name.contains(" ")) {
                    val first = name.split(" ").first().take(1)
                    val second = name.split(" ")[1].take(1)
                    "$first$second"
                } else {
                    name.take(1).uppercase()
                }
            } catch (e: Exception) {
                Log.e("BebasLogger", "GET INITIAL -> ${e.message}")
                "-"
            }
        }
    }
}