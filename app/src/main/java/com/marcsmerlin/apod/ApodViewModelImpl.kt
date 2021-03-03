package com.marcsmerlin.apod

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodViewModelImpl(
    private val repository: ApodRepository
) : ViewModel(), ApodViewModel {

    private val _status = mutableStateOf<ApodViewModel.Status>(ApodViewModel.Status.Initializing)
    override val status: State<ApodViewModel.Status>
        get() = _status

    private lateinit var _result: MutableState<ApodViewModel.Result>
    override val requestResult: State<ApodViewModel.Result>
        get() = _result

    private fun apodListener(apod: Apod) {
        _result.value = ApodViewModel.Result(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _status.value = ApodViewModel.Status.Failed(error)
    }

    init {
        repository.queueHomeRequest(
            { apod: Apod ->
                _status.value = ApodViewModel.Status.Operational
                _result = mutableStateOf(
                    ApodViewModel.Result(
                        apod = apod,
                        isHome = repository.isHome(),
                        hasNext = repository.hasNextDate(),
                        hasPrevious = repository.hasPreviousDate(),
                    )
                )
            },

            ::errorListener,
        )
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
        if (modelClass.isAssignableFrom(ApodViewModelImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApodViewModelImpl(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}