package com.fadlurahmanf.mapp_example.presentation.keyword

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.mapp_api.data.dto.example.PostModel
import com.fadlurahmanf.mapp_example.R

class PostRecyclerViewAdapter : RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {

    private lateinit var posts: ArrayList<PostModel>
    private lateinit var initialPosts: List<PostModel>

    fun setList(list: ArrayList<PostModel>) {
        initialPosts = list
        posts = list
        notifyItemRangeInserted(0, posts.size)
    }

    fun refreshListByKeyword(keyword: String) {
        if (keyword.isEmpty()) {
            val itemCount = posts.size
            posts.clear()
            notifyItemRangeRemoved(0, itemCount)
            posts.addAll(initialPosts)
            repeat(posts.size){
                val title = posts[it].title
                val body = posts[it].body
                posts[it].titleHtml = title
                posts[it].bodyHtml = body
            }
            notifyItemRangeInserted(0, initialPosts.size)
        } else {
            val newPosts = ArrayList(initialPosts)
            newPosts.removeAll {
                !(it.body.contains(keyword) || it.title.contains(keyword))
            }
            posts = newPosts
            repeat(posts.size) {
                val title = posts[it].title
                val body = posts[it].body
                posts[it].titleHtml = title.replace(keyword, "<font color='red'>$keyword</font>")
                posts[it].bodyHtml = body.replace(keyword, "<font color='red'>$keyword</font>")
            }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_title)
        val body: TextView = view.findViewById(R.id.tv_body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.title.text = Html.fromHtml(post.titleHtml)
        holder.body.text = Html.fromHtml(post.bodyHtml)
    }
}