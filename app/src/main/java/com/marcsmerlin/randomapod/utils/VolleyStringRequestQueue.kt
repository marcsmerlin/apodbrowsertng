package com.marcsmerlin.randomapod.utils

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class VolleyStringRequestQueue(
    context: Context
) :
    StringRequestQueue {
    private val queue = Volley.newRequestQueue(context)
    private val tag = this::class.java

    override fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    ) {
        Log.d("$tag", "Queuing string request for $url")

        val request = StringRequest(
            Request.Method.GET,
            url,
            { string ->
                Log.d("$tag", "String received for $url")
                stringListener(string)
            },
            { exception ->
                Log.e("$tag", "Error downloading string for $url:\n$exception")
                errorListener(exception)
            },
        )
        request.tag = tag
        queue.add(request)
    }

    override fun close() {
        queue.cancelAll(tag)
    }
}