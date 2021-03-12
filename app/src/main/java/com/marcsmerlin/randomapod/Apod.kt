package com.marcsmerlin.randomapod

import org.json.JSONObject
import java.time.LocalDate

data class Apod(
    val date: String,
    val title: String,
    val mediaType: String,
    val url: String,
    val explanation: String,
    val hdUrl: String,
    val copyrightInfo: String,
    val serviceVersion: String,
    val thumbnailUrl: String,
) {
    constructor(json: JSONObject) : this(
        date = json.optString("date"),
        title = json.optString("title"),
        mediaType = json.optString("media_type"),
        url = json.optString("url"),
        explanation = json.optString("explanation"),
        hdUrl = json.optString("hdurl"),
        copyrightInfo = json.optString("copyright"),
        serviceVersion = json.optString("service_version"),
        thumbnailUrl = json.optString("thumbnail_url"),
    )

    constructor(string: String) : this(JSONObject(string))

    val localDate: LocalDate
        get() = LocalDate.parse(date)

    fun isImage(): Boolean = mediaType == "image"
    fun isVideo(): Boolean = mediaType == "video"
    fun hasCopyrightInfo(): Boolean = copyrightInfo != ""
    fun hasThumbnail(): Boolean = thumbnailUrl != ""
}