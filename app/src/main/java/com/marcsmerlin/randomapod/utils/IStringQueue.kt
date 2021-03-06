package com.marcsmerlin.randomapod.utils

interface IStringQueue {

    fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    )

    fun close()
}