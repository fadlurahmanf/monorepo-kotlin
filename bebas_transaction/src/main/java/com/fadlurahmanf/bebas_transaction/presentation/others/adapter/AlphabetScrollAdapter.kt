package com.fadlurahmanf.bebas_transaction.presentation.others.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_transaction.R

class AlphabetScrollAdapter : RecyclerView.Adapter<AlphabetScrollAdapter.ViewHolder>() {
    lateinit var context: Context
    private var alphabets: ArrayList<String> = arrayListOf()
    private var callback: Callback? = null

    fun setAlphabets() {
        alphabets.clear()
        alphabets.addAll(
            arrayListOf(
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
                "G",
                "H",
                "I",
                "J",
                "K",
                "L",
                "M",
                "O",
                "P",
                "Q",
                "R",
                "S",
                "T",
                "U",
                "V",
                "W",
                "X",
                "Y",
                "Z"
            )
        )
        notifyItemRangeInserted(0, alphabets.size)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val alphabet = view.findViewById<TextView>(R.id.tv_alphabet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }

        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_alphabet_scroll, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = alphabets.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alphabet = alphabets[position]

        holder.alphabet.text = alphabet

        holder.itemView.setOnTouchListener { v, event ->
            callback?.onAlphabetTouched(v, event, alphabet)
            return@setOnTouchListener false
        }
    }

    interface Callback {
        fun onAlphabetTouched(view: View, event: MotionEvent, alphabet: String)
    }
}
