package com.moustafa.imagefeedpreviewer.repository.unsplash

import com.moustafa.imagefeedpreviewer.BuildConfig
import com.moustafa.imagefeedpreviewer.models.PhotoInfoUnsplashResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * This is the Unsplash service that will include all the network calls that will be done with the
 * Unsplash service
 *
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */
interface UnsplashService {

    @Headers("Authorization: Client-ID ${BuildConfig.API_ACCESS_KEY}")
    @GET("photos")
    suspend fun fetchUnsplashImagesList(
        @Query("page") contentType: Int
    ): Response<List<PhotoInfoUnsplashResponse>>
}