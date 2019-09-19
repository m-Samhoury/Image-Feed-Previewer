package com.moustafa.imagefeedpreviewer.utils

import android.content.res.Resources

/**
 * @author moustafasamhoury
 * created on Wednesday, 11 Sep, 2019
 */
/**
 * Returns the pixel representation of the number according to the device(From DP to PX)
 */
fun Int.px() =
    (this * Resources.getSystem().displayMetrics.density).toInt()
