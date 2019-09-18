package com.moustafa.imagefeedpreviewer.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import kotlinx.coroutines.CoroutineScope

/**
 * @author moustafasamhoury
 * created on Saturday, 11 May, 2019
 */


/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class ImageFeedDataSourceFactory<T>(
    private val scope: CoroutineScope,
    private val adapter: ImageFeedAdapter<T>,
    private val networkState: MutableLiveData<StateMonitor<List<PhotoInfo>>>
) : DataSource.Factory<Int, PhotoInfo>() {

    override fun create(): DataSource<Int, PhotoInfo> =
        ImageFeedDataSource(scope, adapter, networkState)

    companion object {
        fun <T> create(
            scope: CoroutineScope,
            adapter: ImageFeedAdapter<T>,
            errorsLiveData: MutableLiveData<StateMonitor<List<PhotoInfo>>>
        ) =
            ImageFeedDataSource(scope, adapter, errorsLiveData)
    }
}
