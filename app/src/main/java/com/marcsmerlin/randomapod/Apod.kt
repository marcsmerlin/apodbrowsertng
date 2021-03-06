package com.marcsmerlin.randomapod

import org.json.JSONObject
import java.time.LocalDate

/*
Apod: data class which encapsulates the response to a single NASA Apod API request.
 */
data class Apod(
    val date: String,
    val title: String,
    val mediaType: String,
    val url: String,
    val explanation: String,
    val hdurl: String,
    val copyrightInfo: String,
    val serviceVersion: String,
) {
    constructor(json: JSONObject) : this(
        date = json.optString("date"),
        title = json.optString("title"),
        mediaType = json.optString("media_type"),
        url = json.optString("url"),
        explanation = json.optString("explanation"),
        hdurl = json.optString("hdurl"),
        copyrightInfo = json.optString("copyright"),
        serviceVersion = json.optString("service_version"),
    )

    constructor(string: String) : this(JSONObject(string))

    val localDate: LocalDate
        get() = LocalDate.parse(date)

    fun isImage(): Boolean = mediaType == "image"
    fun isVideo(): Boolean = mediaType == "video"
    fun hasCopyrightInfo(): Boolean = copyrightInfo != ""
    fun hasServiceVersion(): Boolean = serviceVersion != ""
}