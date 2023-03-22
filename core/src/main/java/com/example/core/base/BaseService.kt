package com.example.core.base

import com.example.core.utils.Resource
import com.example.core.utils.RetrofitConfig
import com.example.core.utils.ext.logError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseService {

    companion object{

        fun <T> createService(serviceClass: Class<T>, url: String, isDebug: Boolean): T = RetrofitConfig
            .getRetrofitBuilder(url, isDebug)
            .create(serviceClass)
    }

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> = call().let { response ->
        try {
            if(response.isSuccessful)
                Resource.Success(response.body())
            else
                responseError(exception = HttpException(response), message = response.message(), code = response.code())
        } catch (e: Exception) {
            // close request
            response.errorBody()?.close()

            // return response error to ui
            responseError(exception = e, message = e.message ?: e.toString())
        }
    }

    private fun <T> responseError(message: String, code: Int? = null, exception: Exception? = null): Resource<T> {
        val tmpMsg = message.ifEmpty { "Something went wrong!" }

        logError(tmpMsg, exception)

        return Resource.Failure(when(exception){
            is IOException -> Resource.Failure.ErrorHolder.NetworkConnection("No internet connection")

            else -> exception.handleResponseError(code, tmpMsg)
        })
    }

    /**
     * handle response error from body request data.
     * just add some http code on case HttpException
     */
    private fun Exception?.handleResponseError(code: Int?, message: String): Resource.Failure.ErrorHolder = when(this){
        is HttpException -> when(code){
            401 -> Resource.Failure.ErrorHolder.UnAuthorized(message)

            else -> Resource.Failure.ErrorHolder.InternalServerError(message)
        }

        else -> Resource.Failure.ErrorHolder.InternalServerError(message)
    }
}