package com.moustafa.imagefeedpreviewer.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */
@JsonClass(generateAdapter = true)
data class PhotoInfoUnsplashResponse(
    @field:Json(name = "id")
    val id: String? = null,
    @field:Json(name = "color")
    val mainColor: String? = null,
    @field:Json(name = "alt_description")
    val description: String? = null,
    @field:Json(name = "urls")
    val imageUrls: ImageUrls? = null
)

@JsonClass(generateAdapter = true)
data class ImageUrls(
    @field:Json(name = "small")
    val small: String,
    @field:Json(name = "full")
    val full: String
)