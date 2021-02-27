package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel(), IApodViewModel {

    private val _status = mutableStateOf<ApodStatus>(ApodLoading)
    override val status: State<ApodStatus>
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

    override fun goHome() {
        repository.queueHomeRequest(
            ::apodListener,
            ::errorListener,
        )
    }

    override fun getNext() {
        repository.queueRequestForNextDate(
            ::apodListener,
            ::errorListener,
        )
    }

    override fun getPrevious() {
        repository.queueRequestForPreviousDate(
            ::apodListener,
            ::errorListener,
        )
    }

    override fun getRandom() {
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

class ApodViewModelFactory(
    private val repository: ApodRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApodViewModel(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}