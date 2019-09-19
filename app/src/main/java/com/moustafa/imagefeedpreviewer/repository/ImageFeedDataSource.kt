package com.moustafa.imagefeedpreviewer.repository

import android.annotation.SuppressLint
import android.util.Log
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
) : PageKeyedDataSource<Int, PhotoInfo>() {

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
        Log.d("ImageFeedDataSource", "loadInitial()")
        if (networkState.value == StateMonitor.Loading) {
            Log.d("ImageFeedDataSource", "loadInitial() called while statemonitor is loading")
            return
        }

        networkState.postValue(StateMonitor.Loading)

        scope.launch {
            val photosList =
                imageFeedAdapter.fetchImages(page = 1, perPage = params.requestedLoadSize) {
                    networkState.postValue(StateMonitor.Failed(it, retry))

                }

            if (photosList != null) {
                callback.onResult(photosList.photos, 0, 2)
                networkState.postValue(StateMonitor.Loaded(photosList.photos))
            }

        }

    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoInfo>) {
        if (networkState.value == StateMonitor.Loading) {
            Log.d("ImageFeedDataSource", "loadAfter() called while statemonitor is loading")
            return
        }

        networkState.postValue(StateMonitor.Loading)

        scope.launch {
            Log.d("ImageFeedDataSource", "loadAfter()")

            val photosList = imageFeedAdapter.fetchImages(
                perPage = params.requestedLoadSize, page = params.key
            ) {
                networkState.postValue(StateMonitor.Failed(it, retry))
            }
            Log.d(
                "ImageFeedDataSource",
                "loadAfter callback.onResult called with photolist size: ${photosList?.photos?.size}"
            )
            if (photosList != null) {
                callback.onResult(photosList?.photos, params.key + 1)
            }
            networkState.value = StateMonitor.Loaded(photosList?.photos ?: emptyList())
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoInfo>) {
        if (networkState.value == StateMonitor.Loading) {
            Log.d("ImageFeedDataSource", "loadBefore() called while statemonitor is loading")
            return
        }

        networkState.postValue(StateMonitor.Loading)

        scope.launch {
            Log.d("ImageFeedDataSource", "loadBefore()")

            val photosList = imageFeedAdapter.fetchImages(
                perPage = params.requestedLoadSize, page = params.key
            ) {
                networkState.postValue(StateMonitor.Failed(it, retry))
            }
            Log.d(
                "ImageFeedDataSource",
                "loadBefore callback.onResult called with photolist size: ${photosList?.photos?.size}"
            )
            if (photosList != null) {
                callback.onResult(photosList.photos, if (params.key > 1) params.key - 1 else null)
            }
            networkState.value = StateMonitor.Loaded(photosList?.photos ?: emptyList())
        }
    }

    companion object {
        val TAG = PageKeyedDataSource::class.java.name
    }
}