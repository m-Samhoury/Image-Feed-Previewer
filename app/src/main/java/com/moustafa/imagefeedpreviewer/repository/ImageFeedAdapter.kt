package com.moustafa.imagefeedpreviewer.repository

import com.moustafa.imagefeedpreviewer.models.PhotoInfo
import com.moustafa.imagefeedpreviewer.models.PhotoInfoList
import okhttp3.Headers
import retrofit2.Response

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

interface ImageFeedAdapter<in R> {

    val mapper: (R) -> PhotoInfo

    suspend fun fetchImages(perPage: Int, page: Int, onError: (Exception) -> Any): PhotoInfoList?

    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>,
        onError: (Exception) -> Any
    ): T? {
        val result: Result<T> = safeApiResult(call)
        var data: T? = null

        when (result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                onError(result.exception)
            }
        }
        return data
    }

    suspend fun <T : Any> safeApiCallWithHeader(
        call: suspend () -> Response<T>,
        onError: (Exception) -> Any
    ): Pair<T?, Headers?> {
        val result: Result<T> = safeApiResult(call)
        var data: T? = null
        var headers: Headers? = null


        when (result) {
            is Result.Success -> {
                data = result.data
                headers = result.headers
            }
            is Result.Error -> {
                onError(result.exception)
            }
        }
        return Pair(data, headers)
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>
    ): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) return Result.Success(response.body()!!, response.headers())

            return Result.Error(java.lang.Exception())
        } catch (exception: Exception) {
            return Result.Error(exception)
        }
    }
}

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T, val headers: Headers? = null) :
        Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()
}