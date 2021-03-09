package com.marcsmerlin.randomapod

import androidx.compose.runtime.State

interface ApodViewModel {
    val status: State<Status>
    val result: State<Result>
    val isToday: State<Boolean>

    sealed class Status {
        object Initializing : Status()
        data class InitializationFailure(val error: Exception) : Status()
        object Operational : Status()
    }

    sealed class Result {
        data class Data(val apod: Apod): Result()
        data class Error(val error: Exception): Result()
    }

    fun goToday()

    fun getRandom()

    fun onCleared()
}