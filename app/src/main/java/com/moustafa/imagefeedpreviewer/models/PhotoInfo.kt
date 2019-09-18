package com.moustafa.imagefeedpreviewer.models

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

data class PhotoInfo(
    val id: String,
    val mainColor: String? = null,
    val description: String? = null,
    val smallImageUrl: String,
    val fullImageUrl: String? = null
)