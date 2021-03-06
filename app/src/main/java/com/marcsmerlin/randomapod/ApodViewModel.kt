package com.marcsmerlin.randomapod

import androidx.compose.runtime.State

/*
ApodViewModel: interface for viewModels that conform to ApodUI requirements.
 */
interface ApodViewModel {
    val status: State<Status>
    val result: State<Result>

    sealed class Status {
        object Initializing : Status()
        object Operational : Status()
    }

    sealed class Result {
        data class Data(val apod: Apod): Result()
        data class Error(val error: Exception): Result()
    }

    fun isHome(): Boolean
    fun goHome()

    fun hasNext(): Boolean
    fun getNext()

    fun hasPrevious(): Boolean
    fun getPrevious()

    fun getRandom()

    fun onCleared()
}