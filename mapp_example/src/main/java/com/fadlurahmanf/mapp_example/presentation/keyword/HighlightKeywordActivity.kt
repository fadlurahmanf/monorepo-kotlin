package com.fadlurahmanf.mapp_example.presentation.keyword

import android.text.Editable
import android.text.TextWatcher
import com.fadlurahmanf.mapp_api.data.dto.example.PostModel
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_example.databinding.ActivityHighlightKeywordBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class HighlightKeywordActivity :
    BaseExampleActivity<ActivityHighlightKeywordBinding>(ActivityHighlightKeywordBinding::inflate) {

    @Inject
    lateinit var viewModel: HighlightKeywordViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: PostRecyclerViewAdapter
    private val posts: ArrayList<PostModel> = arrayListOf()
    private val searchPosts: ArrayList<PostModel> = arrayListOf()

    override fun setup() {
        binding.etKeyword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.refreshListByKeyword(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        adapter = PostRecyclerViewAdapter()
        adapter.setList(searchPosts)
        binding.rv.adapter = adapter

        viewModel.posts.observe(this) { state ->
            when (state) {
                is NetworkState.SUCCESS -> {
                    posts.clear()
                    searchPosts.clear()
                    searchPosts.addAll(state.data.map { resp ->
                        PostModel(
                            title = resp.title ?: "",
                            titleHtml = resp.title ?: "",
                            body = resp.body ?: "",
                            bodyHtml = resp.body ?: "",
                        )
                    })
                    posts.addAll(state.data.map { resp ->
                        PostModel(
                            title = resp.title ?: "",
                            titleHtml = resp.title ?: "",
                            body = resp.body ?: "",
                            bodyHtml = resp.body ?: "",
                        )
                    })
                    adapter.setList(searchPosts)
                }

                else -> {

                }
            }
        }

        viewModel.getListPost()
    }

}