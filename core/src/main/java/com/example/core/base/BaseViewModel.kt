package com.example.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.utils.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel(), CoroutineScope{

    private val supervisorJob = SupervisorJob()

    override val coroutineContext: CoroutineContext get() = dispatcher + supervisorJob

    val eventMessage = MutableLiveData<String>()
    val eventRestart = MutableLiveData<Boolean>()

    fun onError(error: Resource.Failure.ErrorHolder?){
        error?.let { err -> eventMessage.postValue(err.message) }
    }

    /**
     * Handle response from remote data.
     * In this case, handle response code token expired to inform ui
     */
    suspend fun <T> Resource<T>.getResultCase(callback: suspend (result: Resource<T>) -> Unit){
        withContext(Dispatchers.Main){
            if(error != null){
                // handle token expired
                if(error is Resource.Failure.ErrorHolder.UnAuthorized){
                    eventRestart.postValue(true)
                }
            }

            // return result
            callback(this@getResultCase)
        }
    }
}