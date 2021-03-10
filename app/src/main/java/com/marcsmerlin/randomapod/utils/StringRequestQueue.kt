package com.marcsmerlin.randomapod.utils

interface StringRequestQueue {

    fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    )

    fun close()
}