package com.marcsmerlin.apodbrowser

interface IStringQueue {

    fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    )

    fun close()
}