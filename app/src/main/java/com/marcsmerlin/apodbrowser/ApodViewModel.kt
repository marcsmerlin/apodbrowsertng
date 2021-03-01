package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

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

    private val _status = mutableStateOf<Status>(Status.Initializing)
    val status: State<Status>
        get() = _status

    private lateinit var _result: MutableState<Result>
    val requestResult: State<Result>
        get() = _result

    private fun apodListener(apod: Apod) {
        _result.value = Result(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _status.value = Status.Failed(error)
    }

    init {
        repository.queueHomeRequest(
            { apod: Apod ->
                _status.value = Status.Operational
                _result = mutableStateOf(
                    Result(
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