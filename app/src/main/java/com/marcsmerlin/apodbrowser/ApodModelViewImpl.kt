package com.marcsmerlin.apodbrowser

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApodModelViewImpl(
    private val repository: ApodRepository
) : ViewModel(), ApodModelView {

    private val _status = mutableStateOf<ApodModelView.Status>(ApodModelView.Status.Initializing)
    override val status: State<ApodModelView.Status>
        get() = _status

    private lateinit var _result: MutableState<ApodModelView.Result>
    override val requestResult: State<ApodModelView.Result>
        get() = _result

    private fun apodListener(apod: Apod) {
        _result.value = ApodModelView.Result(
            apod = apod,
            isHome = repository.isHome(),
            hasNext = repository.hasNextDate(),
            hasPrevious = repository.hasPreviousDate(),
        )
    }

    private fun errorListener(error: Exception) {
        _status.value = ApodModelView.Status.Failed(error)
    }

    init {
        repository.queueHomeRequest(
            { apod: Apod ->
                _status.value = ApodModelView.Status.Operational
                _result = mutableStateOf(
                    ApodModelView.Result(
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
        if (modelClass.isAssignableFrom(ApodModelViewImpl::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApodModelViewImpl(repository = repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}