package com.marcsmerlin.randomapod

import androidx.compose.runtime.State

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

    fun isToday(): Boolean
    fun goToday()

    fun getRandom()

    fun onCleared()
}