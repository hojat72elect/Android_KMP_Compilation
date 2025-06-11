package ca.hojat.gamehub.core.data.api.common.calladapter

import ca.hojat.gamehub.core.data.api.common.ApiResult
import ca.hojat.gamehub.core.data.api.common.ErrorMessageExtractor
import ca.hojat.gamehub.core.data.api.common.enqueue
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import ca.hojat.gamehub.core.data.api.common.Error as ApiError

class ApiResultCall<T>(
    private val delegate: Call<T>,
    private val successType: Type,
    private val errorMessageExtractor: ErrorMessageExtractor
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        delegate.enqueue(
            onResponse = { _, response ->
                callback.onResponse(
                    this,
                    Response.success(response.toApiResult())
                )
            },
            onFailure = { _, throwable ->
                callback.onResponse(
                    this,
                    Response.success(throwable.toApiResult())
                )
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Response<T>.toApiResult(): ApiResult<T> {
        if (!isSuccessful) {
            val httpCode = code()
            val message = (errorBody()?.extractErrorMessage() ?: "")

            return Err(ApiError.HttpError(httpCode, message))
        }

        body()?.let { return Ok(it) }

        return if (successType == Unit::class.java) {
            (Ok(Unit) as ApiResult<T>)
        } else {
            Err(ApiError.UnknownError(IllegalStateException("The response body was null.")))
        }
    }

    private fun ResponseBody.extractErrorMessage(): String {
        return errorMessageExtractor.extract(string())
    }

    private fun <T> Throwable.toApiResult(): ApiResult<T> {
        return Err(
            when (this) {
                is IOException -> ApiError.NetworkError(this)
                else -> ApiError.UnknownError(this)
            }
        )
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException()
    }

    override fun clone(): Call<ApiResult<T>> {
        return ApiResultCall(delegate.clone(), successType, errorMessageExtractor)
    }
}
