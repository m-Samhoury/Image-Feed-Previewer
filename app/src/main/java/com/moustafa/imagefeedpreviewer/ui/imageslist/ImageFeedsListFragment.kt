package com.moustafa.imagefeedpreviewer.ui.imageslist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.moustafa.imagefeedpreviewer.R
import com.moustafa.imagefeedpreviewer.repository.StateMonitor
import com.moustafa.imagefeedpreviewer.utils.px
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
        ViewCompat.setOnApplyWindowInsetsListener(recyclerViewImagesList) { _, insets ->
            recyclerViewImagesList.setPadding(
                0,
                insets.systemWindowInsetTop + 16.px(),
                0,
                0
            )
            insets.consumeSystemWindowInsets()
        }

        (recyclerViewImagesList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations =
            false
        recyclerViewImagesList.adapter = imageFeedsListAdapter
        val layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recyclerViewImagesList.layoutManager = layoutManager
    }
}
