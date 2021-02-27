package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.State

sealed class ApodStatus

data class ApodSuccess(
    val apod: Apod,
    val isHome: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
) : ApodStatus()

data class ApodError(
    val error: Exception
) : ApodStatus()

object ApodLoading : ApodStatus()

interface IApodViewModel {
    val status: State<ApodStatus>

    fun goHome()
    fun getNext()
    fun getPrevious()
    fun getRandom()
    fun onCleared()
}