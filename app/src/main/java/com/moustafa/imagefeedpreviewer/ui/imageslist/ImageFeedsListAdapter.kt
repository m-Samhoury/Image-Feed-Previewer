package com.moustafa.imagefeedpreviewer.ui.imageslist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.moustafa.imagefeedpreviewer.R
import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import kotlinx.android.synthetic.main.item_feed_image.view.*

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class ImageFeedsListAdapter :
    PagedListAdapter<PhotoInfo, ReposListViewHolder>(PHOTO_INFO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposListViewHolder =
        ReposListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feed_image, parent, false)
        )

    override fun onBindViewHolder(holder: ReposListViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        val PHOTO_INFO_COMPARATOR =
            object : DiffUtil.ItemCallback<PhotoInfo>() {
                override fun areContentsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo): Boolean =
                    oldItem == newItem

                override fun areItemsTheSame(oldItem: PhotoInfo, newItem: PhotoInfo): Boolean =
                    oldItem.id == newItem.id

                override fun getChangePayload(oldItem: PhotoInfo, newItem: PhotoInfo): Any = Any()
            }
    }
}

class ReposListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(photoInfo: PhotoInfo?) {
        if (photoInfo != null) {
            itemView.imageViewPhoto.load(getSuitableImageUrl(photoInfo)) {
                crossfade(true)
            }

            if (photoInfo.mainColor?.isNotBlank() == true) {
                itemView.imageViewPhoto.layoutParams.apply {

                }
                val color = Color.parseColor(photoInfo.mainColor.toString())
                itemView.imageViewPhoto.setBackgroundColor(color)
            } else {
                itemView.imageViewPhoto.background = null
            }
        } else {
            itemView.imageViewPhoto.setImageDrawable(null)
            itemView.imageViewPhoto.setBackgroundColor(Color.RED)
        }
    }

    /**
     *  Top priority is to show an image. It would be ideal if there is a smallImage url
     *  However if not, try to get the fullimage url and load the image.
     *
     *  Though this is not ideal, but a compromise in favor of always displaying an image.
     */
    private fun getSuitableImageUrl(photoInfo: PhotoInfo) =
        when {
            photoInfo.smallImageUrl.isNotBlank() -> photoInfo.smallImageUrl
            photoInfo.fullImageUrl?.isNotBlank() == true -> photoInfo.fullImageUrl
            else -> ""
        }
}