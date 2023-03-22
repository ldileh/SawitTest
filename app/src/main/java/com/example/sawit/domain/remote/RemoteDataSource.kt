package com.example.sawit.domain.remote

import com.example.core.base.BaseService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: RemoteService
): BaseService() {


}