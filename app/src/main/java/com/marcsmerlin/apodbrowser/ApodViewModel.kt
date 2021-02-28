package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    sealed class Status {
        object Initializing : Status()
        data class FailedToInitialize(
            val error: Exception
        ) : Status()
        sealed class Result : Status() {
            data class Success(
                val apod: Apod,
                val isHome: Boolean,
                val hasNext: Boolean,
                val hasPrevious: Boolean,
            ) : Result()
            data class Error(
                val error: Exception
            ) : Result()
        }
    }

    private val _status = mutableStateOf<Status>(Status.Initializing)
    val status: State<Status>
        get() = _status

    private fun apodListener(apod: Apod) {
        _status.value = Status.Result.Success(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _status.value = Status.Result.Error(error)
    }

    init {
        repository.queueHomeRequest(
            ::apodListener,
        ) { error -> _status.value = Status.FailedToInitialize(error) }
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