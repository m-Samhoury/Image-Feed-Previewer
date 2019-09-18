package com.moustafa.imagefeedpreviewer.repository.unsplash

import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import com.moustafa.imagefeedpreviewer.models.PhotoInfoList
import com.moustafa.imagefeedpreviewer.models.PhotoInfoUnsplashResponse
import com.moustafa.imagefeedpreviewer.repository.ImageFeedAdapter

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class UnsplashAdapter(private val service: UnsplashService) :
    ImageFeedAdapter<PhotoInfoUnsplashResponse> {

    override val mapper: (PhotoInfoUnsplashResponse) -> PhotoInfo = {
        PhotoInfo(
            id = it.id!!,
            mainColor = it.mainColor,
            description = it.description,
            fullImageUrl = it.imageUrls?.full,
            smallImageUrl = it.imageUrls?.small!!
        )
    }

    override suspend fun fetchImages(
        perPage: Int,
        page: Int,
        onError: (Exception) -> Any
    ): PhotoInfoList {
        val resultAndHeaderPair = safeApiCallWithHeader({
            service.fetchUnsplashImagesList(perPage = perPage, page = page)
        }, onError)
        val result = resultAndHeaderPair.first
        val headers = resultAndHeaderPair.second
        val photos = result
            ?.filter {
                it.id?.isNotBlank() == true
                        &&
                        (it.imageUrls?.small?.isNotBlank() == true
                                || it.imageUrls?.full?.isNotBlank() == true)
            }?.map(mapper) ?: emptyList()

        val totalPhotosCountString = headers?.get("x-total")
        val totalPhotosResults =
            if (totalPhotosCountString?.isNotBlank() == true) Integer.parseInt(
                totalPhotosCountString
            )
            else photos.size

        return PhotoInfoList(
            photos = photos,
            totalPhotosResults = totalPhotosResults
        )
    }
}
