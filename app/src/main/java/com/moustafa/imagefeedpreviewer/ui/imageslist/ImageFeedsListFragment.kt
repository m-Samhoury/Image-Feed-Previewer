package com.moustafa.imagefeedpreviewer.ui.imageslist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.moustafa.imagefeedpreviewer.R
import com.moustafa.imagefeedpreviewer.repository.StateMonitor
import kotlinx.android.synthetic.main.fragment_image_feeds_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class ImageFeedsListFragment : Fragment(R.layout.fragment_image_feeds_list) {

    private val imageFeedsListViewModel: ImageFeedsListViewModel by viewModel()

    private val imageFeedsListAdapter: ImageFeedsListAdapter by lazy {
        ImageFeedsListAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        imageFeedsListViewModel.imageFeedsListState.observe(this, Observer {
            Log.d("ImageFeedsListFragment", "State:${it.stateMonitor::class.java}")
            handleState(it)
        })
    }

    private fun handleState(state: ImageFeedsListState) = when (state.stateMonitor) {
        StateMonitor.Init -> {
            showLoadingIndicator(false)
        }
        StateMonitor.Loading -> {
            showLoadingIndicator(true)
        }
        is StateMonitor.Loaded -> {
            showLoadingIndicator(false)
            imageFeedsListAdapter.submitList(state.stateMonitor.result)
        }
        is StateMonitor.Failed -> {
            showLoadingIndicator(false)
        }
    }

    private fun showLoadingIndicator(shouldShow: Boolean) {
        if (shouldShow && imageFeedsListAdapter.itemCount == 0) {
            progressBarLoadingImagesInitial.show()
        } else {
            progressBarLoadingImagesInitial.hide()
        }
    }

    private fun setupViews() {
        recyclerViewImagesList.apply {
            layoutManager = GridLayoutManager(
                context,
                6,
                GridLayoutManager.VERTICAL,
                false
            )
//                .apply {
//                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
//            }
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            adapter = imageFeedsListAdapter
        }
    }
}
