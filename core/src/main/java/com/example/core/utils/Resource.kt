package com.example.core.utils

sealed class Resource<T>(val data: T? = null, val error: Failure.ErrorHolder? = null){

    class Success<T>(data: T?) : Resource<T>(data = data)
    class Failure<T>(error: ErrorHolder?): Resource<T>(error = error){

        @Suppress("unused")
        sealed class ErrorHolder(val message: String){

            class NetworkConnection(message: String): ErrorHolder(message)
            class BadRequest(message: String): ErrorHolder(message)
            class UnAuthorized(message: String): ErrorHolder(message)
            class InternalServerError(message: String): ErrorHolder(message)
            class ResourceNotFound(message: String): ErrorHolder(message)
        }
    }
}