package com.marcsmerlin.apod.utils

interface IStringQueue {

    fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    )

    fun close()
}