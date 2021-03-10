package com.marcsmerlin.randomapod

import com.marcsmerlin.randomapod.utils.StringRequestQueue
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class ApodArchive(
    endpoint: String,
    apiKey: String,
    firstDate: String,
    private val requestQueue: StringRequestQueue
) {
    private val firstDate = LocalDate.parse(firstDate)
    private val baseUrl = "${endpoint}?api_key=$apiKey&thumbs=true"

    private lateinit var todayDate: LocalDate
    private lateinit var currentDate: LocalDate

    fun queueTodayRequest(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        requestQueue.addStringRequest(
            baseUrl,
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                todayDate = currentDate
                apodListener(apod)
            },
            errorListener,
        )
    }

    fun isToday(): Boolean {
        return currentDate.isEqual(todayDate)
    }

    fun hasNextDate(): Boolean {
        return currentDate.isBefore(todayDate)
    }

    private fun urlForDate(date: LocalDate): String {
        return "${baseUrl}&date=$date"
    }

    fun queueRequestForNextDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        if (hasNextDate()) {
            requestQueue.addStringRequest(
                urlForDate(currentDate.plusDays(1L)),
                { string ->
                    val apod = Apod(string)
                    currentDate = apod.localDate
                    apodListener(apod)
                },
                errorListener,
            )
        }
    }

    fun hasPreviousDate(): Boolean {
        return currentDate.isAfter(firstDate)
    }

    fun queueRequestForPreviousDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        if (hasPreviousDate()) {
            requestQueue.addStringRequest(
                urlForDate(currentDate.minusDays(1L)),
                { string ->
                    val apod = Apod(string)
                    currentDate = apod.localDate
                    apodListener(apod)
                },
                errorListener,
            )
        }
    }

    fun queueRequestForRandomDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        val daysSinceFirstDate = ChronoUnit.DAYS.between(firstDate, todayDate)
        val randomDate = firstDate.plusDays(Random.nextLong(until = daysSinceFirstDate))

        requestQueue.addStringRequest(
            urlForDate(randomDate),
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                apodListener(apod)
            },
            errorListener
        )
    }

    fun close() {
        requestQueue.close()
    }
}