package com.fadlurahmanf.bebas_loyalty.presentation.program.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.bebas_loyalty.R
import com.fadlurahmanf.bebas_loyalty.data.dto.ProgramCategoryModel
import com.fadlurahmanf.bebas_shared.extension.toDp

class ProgramCategoryAdapter : RecyclerView.Adapter<ProgramCategoryAdapter.ViewHolder>() {

    lateinit var context: Context
    private val categories: ArrayList<ProgramCategoryModel> = arrayListOf()
    var selectedCategoryId: String? = null
    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setList(list: List<ProgramCategoryModel>) {
        categories.clear()
        categories.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    fun selectCategory(id: String) {
        if (selectedCategoryId == id) {
            return
        }

        var index = -1
        for (i in categories.indices) {
            if (categories[i].isSelected) {
                index = i
                break
            }
        }
        if (index != -1) {
            selectedCategoryId = null
            categories[index].isSelected = false
            notifyItemChanged(index)
        }

        index = -1
        for (i in categories.indices) {
            if (categories[i].id == id) {
                index = i
                break
            }
        }
        if (index != -1) {
            categories[index].isSelected = true
            notifyItemChanged(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.findViewById<TextView>(R.id.tv_label)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgramCategoryAdapter.ViewHolder {
        if (!::context.isInitialized) {
            context = parent.context
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program_category_label, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        val mlp = holder.label.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 0) {
            mlp.setMargins(0, 0, 0, 0)
        } else if (position == (categories.size - 1)) {
            mlp.setMargins(5.toDp(), 0, 0, 0)
        } else {
            mlp.setMargins(5.toDp(), 0, 5.toDp(), 0)
        }

        holder.label.layoutParams = mlp
        holder.label.text = category.label

        if (category.isSelected) {
            holder.label.background = ContextCompat.getDrawable(
                context,
                R.drawable.background_item_program_category_label_selected
            )
            holder.label.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.label.background = ContextCompat.getDrawable(
                context,
                R.drawable.background_item_program_category_label_unselected
            )
            holder.label.setTextColor(ContextCompat.getColor(context, R.color.grey))
        }


        holder.itemView.setOnClickListener {
            callback?.onProgramCategoryClicked(category)
        }
    }

    interface Callback {
        fun onProgramCategoryClicked(category: ProgramCategoryModel)
    }
}