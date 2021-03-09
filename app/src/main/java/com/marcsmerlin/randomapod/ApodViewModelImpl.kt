package com.marcsmerlin.randomapod

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodViewModelImpl(
    private val repository: ApodRepository
) : ViewModel(), ApodViewModel {

    private val _status = mutableStateOf<ApodViewModel.Status>(
        ApodViewModel.Status.Initializing
    )

    override val status: State<ApodViewModel.Status>
        get() = _status

    private lateinit var _result: MutableState<ApodViewModel.Result>

    override val result: State<ApodViewModel.Result>
        get() = _result

    private lateinit var _isToday: MutableState<Boolean>

    override val isToday: State<Boolean>
        get() = _isToday

    private fun apodListener(apod: Apod) {
        _result.value = ApodViewModel.Result.Data(apod)
        _isToday.value = repository.isToday()
    }

    private fun errorListener(error: Exception) {
        _result.value = ApodViewModel.Result.Error(error)
    }

    init {
        repository.queueTodayRequest(
            { apod: Apod ->
                _status.value = ApodViewModel.Status.Operational
                _result = mutableStateOf(ApodViewModel.Result.Data(apod))
                _isToday = mutableStateOf(true)
            },

            { error: Exception ->
                _status.value = ApodViewModel.Status.InitializationFailure(error)
            },
        )
    }

    override fun goToday() {
        repository.queueTodayRequest(
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