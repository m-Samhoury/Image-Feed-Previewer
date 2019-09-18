package com.moustafa.imagefeedpreviewer.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author moustafasamhoury
 * created on Saturday, 11 May, 2019
 */

class ImageFeedDataSource<T>(
    private val scope: CoroutineScope,
    private val imageFeedAdapter: ImageFeedAdapter<T>,
    private val networkState: MutableLiveData<StateMonitor<List<PhotoInfo>>>
) :
    PageKeyedDataSource<Int, PhotoInfo>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    @SuppressLint("CheckResult")
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoInfo>
    ) {
        if (networkState.value == StateMonitor.Loading) {
            return
        }

        networkState.postValue(StateMonitor.Loading)

        scope.launch {
            val photosList = imageFeedAdapter.fetchImages(page = 1) {
                networkState.postValue(StateMonitor.Failed(it, retry))

            }

            if (photosList != null) {
                callback.onResult(photosList, 0, 2)
                networkState.postValue(StateMonitor.Loaded(photosList))
            }

        }

    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoInfo>) {
        if (networkState.value == StateMonitor.Loading) {
            return
        }

        networkState.postValue(StateMonitor.Loading)

        scope.launch {
            val photosList = imageFeedAdapter.fetchImages(page = params.key) {
                networkState.postValue(StateMonitor.Failed(it, retry))
            }

            if (photosList != null) {
                callback.onResult(photosList, params.key + 1)
            }
        }


    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoInfo>) {
        // ignored, since we only ever append to our initial load
    }


    companion object {
        val TAG = PageKeyedDataSource::class.java.name
    }

}