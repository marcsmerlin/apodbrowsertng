package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

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

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    private val _status = mutableStateOf<ApodStatus>(ApodLoading)
    val status: State<ApodStatus>
        get() = _status

    private fun apodListener(apod: Apod) {
        _status.value = ApodSuccess(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _status.value = ApodError(error)
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

    override fun onCleared() {
        super.onCleared()
        repository.close()
    }
}

class ApodViewModelFactory(private val repository: ApodRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodViewModel::class.java)) {
            return ApodViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}