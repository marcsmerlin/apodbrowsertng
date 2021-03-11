package com.marcsmerlin.randomapod.utils

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.UUID

class VolleyStringRequestQueue(
    context: Context
) :
    StringRequestQueue {
    private val queue = Volley.newRequestQueue(context)
    private val queueTag = UUID.randomUUID()

    override fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    ) {
        val logTag = this::class.java
        Log.i("$logTag", "Queuing string request for: $url")

        val request = StringRequest(
            Request.Method.GET,
            url,
            { string -> stringListener(string) },
            { exception ->
                Log.e("$logTag", "Error downloading string for $url:\n$exception")
                errorListener(exception)
            },
        )
        request.tag = queueTag
        queue.add(request)
    }

    override fun close() {
        queue.cancelAll(queueTag)
    }
}