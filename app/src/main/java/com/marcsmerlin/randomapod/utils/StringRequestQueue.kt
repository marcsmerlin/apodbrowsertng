package com.marcsmerlin.randomapod.utils

interface StringRequestQueue {

    fun queueRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    )

    fun close()
}