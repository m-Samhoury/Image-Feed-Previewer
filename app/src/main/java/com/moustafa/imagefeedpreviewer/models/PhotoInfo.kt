package com.moustafa.imagefeedpreviewer.models

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

data class PhotoInfo(
    val id: String,
    val mainColor: String? = null,
    val description: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val smallImageUrl: String,
    val fullImageUrl: String? = null
) {
    val aspectRatio: Double =
        if (width != null && height != null) height.toDouble() / width.toDouble() else -1.0
}