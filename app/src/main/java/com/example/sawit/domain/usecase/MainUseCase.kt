package com.example.sawit.domain.usecase

import com.example.core.base.BaseUseCase
import com.example.sawit.domain.local.LocalDataSource
import com.example.sawit.domain.remote.RemoteDataSource
import javax.inject.Inject

@Suppress("unused")
class MainUseCase @Inject constructor(
    private val remoteData: RemoteDataSource,
    private val localData: LocalDataSource
): BaseUseCase()