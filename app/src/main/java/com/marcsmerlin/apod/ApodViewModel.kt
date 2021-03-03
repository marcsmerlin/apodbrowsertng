package com.marcsmerlin.apod

import androidx.compose.runtime.State

interface ApodViewModel {
    val status: State<Status>
    val requestResult: State<Result>

    sealed class Status {
        object Initializing : Status()
        data class Failed(val error: Exception) : Status()
        object Operational : Status()
    }

    data class Result(
        val apod: Apod,
        val isHome: Boolean,
        val hasNext: Boolean,
        val hasPrevious: Boolean,
    )

    fun goHome()
    fun getNext()
    fun getPrevious()
    fun getRandom()
    fun onCleared()
}