package com.marcsmerlin.randomapod.utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.UUID

class VolleyStringQueue(
    context: Context
) :
    IStringQueue {
    private val queue = Volley.newRequestQueue(context)
    private val queueTag = UUID.randomUUID()

    override fun addStringRequest(
        url: String,
        stringListener: (String) -> Unit,
        errorListener: (Exception) -> Unit,
    ) {
        val request = StringRequest(
            Request.Method.GET,
            url,
            { string -> stringListener(string) },
            { exception -> errorListener(exception) },
        )
        request.tag = queueTag
        queue.add(request)
    }

    override fun close() {
        queue.cancelAll(queueTag)
    }
}