package com.moustafa.imagefeedpreviewer.ui.imageslist

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import com.moustafa.imagefeedpreviewer.models.PhotoInfoUnsplashResponse
import com.moustafa.imagefeedpreviewer.repository.ImageFeedAdapter
import com.moustafa.imagefeedpreviewer.repository.ImageFeedDataSourceFactory
import com.moustafa.imagefeedpreviewer.repository.StateMonitor

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */
data class ImageFeedsListState(val stateMonitor: StateMonitor<PagedList<PhotoInfo>>)

class ImageFeedsListViewModel(
    private val imageFeedAdapter: ImageFeedAdapter<PhotoInfoUnsplashResponse>,
    private val imageFeedsState: ImageFeedsListState = ImageFeedsListState(StateMonitor.Init)
) : ViewModel() {

    val imageFeedsListState: LiveData<ImageFeedsListState>

    private val pagedLiveData by lazy {
        initializedPagedListBuilder()
    }

    private val paginatedFeedsLiveData = MutableLiveData<StateMonitor<List<PhotoInfo>>>()

    init {
        val errorsLiveData = Transformations.map(paginatedFeedsLiveData) {
            when (it) {
                StateMonitor.Loading -> imageFeedsState.copy(stateMonitor = StateMonitor.Loading)
                StateMonitor.Init -> when (imageFeedsState.stateMonitor) {
                    StateMonitor.Loading -> imageFeedsState
                    StateMonitor.Init -> imageFeedsState
                    is StateMonitor.Loaded -> imageFeedsState
                    is StateMonitor.Failed -> imageFeedsState.copy(stateMonitor = StateMonitor.Init)
                }
                is StateMonitor.Loaded -> imageFeedsState
                is StateMonitor.Failed ->
                    imageFeedsState.copy(stateMonitor = StateMonitor.Failed(it.failed, it.action))
            }
        }

        val liveData = Transformations.map(pagedLiveData) {
            imageFeedsState.copy(stateMonitor = StateMonitor.Loaded(it))
        }

        val mediatorLiveData = MediatorLiveData<ImageFeedsListState>()

        mediatorLiveData.addSource(errorsLiveData) {
            mediatorLiveData.value = it
        }

        mediatorLiveData.addSource(liveData) {
            mediatorLiveData.value = it
        }
        imageFeedsListState = mediatorLiveData
    }

    private fun initializedPagedListBuilder(): LiveData<PagedList<PhotoInfo>> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(60)
            .setPageSize(20)
            .setPrefetchDistance(20)
            .setMaxSize(80)
            .setEnablePlaceholders(true)
            .build()
        return ImageFeedDataSourceFactory(
            viewModelScope,
            imageFeedAdapter,
            paginatedFeedsLiveData
        ).toLiveData(config)
    }
}
