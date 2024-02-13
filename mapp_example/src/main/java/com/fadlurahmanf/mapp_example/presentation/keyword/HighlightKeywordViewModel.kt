package com.fadlurahmanf.mapp_example.presentation.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.mapp_api.data.datasources.JsonPlaceHolderRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.example.PostResponse
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_api.external.helper.network_state.NetworkState
import com.fadlurahmanf.mapp_ui.external.helper.view_model.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class HighlightKeywordViewModel @Inject constructor(
    private val jsonPlaceHolderRemoteDatasource: JsonPlaceHolderRemoteDatasource
) : BaseViewModel() {

    private val _posts = MutableLiveData<NetworkState<List<PostResponse>>>()
    val posts: LiveData<NetworkState<List<PostResponse>>> = _posts

    fun getListPost() {
        _posts.value = NetworkState.LOADING
        compositeDisposable().add(jsonPlaceHolderRemoteDatasource.getListPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _posts.value = NetworkState.SUCCESS(it)
                },
                {
                    _posts.value = NetworkState.FAILED(MappException.fromThrowable(it))
                },
                {}
            ))
    }
}