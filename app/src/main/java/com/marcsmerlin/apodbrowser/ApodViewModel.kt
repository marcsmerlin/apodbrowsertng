package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

sealed class ApodResult

data class ApodSuccess(
    val apod: Apod,
    val isHome: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean,
) : ApodResult()

data class ApodError(
    val error: Exception
) : ApodResult()

object ApodLoading : ApodResult()

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    private val _result = mutableStateOf<ApodResult>(ApodLoading)
    val result: State<ApodResult>
        get() = _result

    private fun apodListener(apod: Apod) {
        _result.value = ApodSuccess(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _result.value = ApodError(error)
    }

    init {
        goHome()
    }

    fun goHome() {
        repository.queueHomeRequest(
            ::apodListener,
            ::errorListener,
        )
    }

    fun getNext() {
        repository.queueRequestForNextDate(
            ::apodListener,
            ::errorListener,
        )
    }

    fun getPrevious() {
        repository.queueRequestForPreviousDate(
            ::apodListener,
            ::errorListener,
        )
    }

    fun getRandom() {
        repository.queueRequestForRandomDate(
            ::apodListener,
            ::errorListener,
        )
    }
}