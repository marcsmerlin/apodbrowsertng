package com.marcsmerlin.apodbrowser

import com.marcsmerlin.apodbrowser.utils.IStringQueue
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class ApodRepository(
    endpoint: String,
    apiKey: String,
    firstDate: String,
    private val queue: IStringQueue
) {

    private val firstDate = LocalDate.parse(firstDate)
    private val baseUrl = "${endpoint}?api_key=$apiKey"

    private lateinit var homeDate: LocalDate
    private lateinit var currentDate: LocalDate

    init {
        queue.addStringRequest(
            baseUrl,
            { string ->
                currentDate = Apod(string).localDate
                homeDate = currentDate
            },
            { exception -> throw exception },
        )
    }

    fun queueHomeRequest(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        queue.addStringRequest(
            baseUrl,
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                apodListener(apod)
            },
            errorListener,
        )
    }

    fun isHome(): Boolean {
        return currentDate.isEqual(homeDate)
    }

    fun hasNextDate(): Boolean {
        return currentDate.isBefore(homeDate)
    }

    private fun urlForDate(date: LocalDate): String {
        return "${baseUrl}&date=$date"
    }

    fun queueRequestForNextDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        queue.addStringRequest(
            urlForDate(currentDate.plusDays(1L)),
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                apodListener(apod)
            },
            errorListener,
        )
    }

    fun hasPreviousDate(): Boolean {
        return currentDate.isAfter(firstDate)
    }

    fun queueRequestForPreviousDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        queue.addStringRequest(
            urlForDate(currentDate.minusDays(1L)),
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                apodListener(apod)
            },
            errorListener,
        )
    }

    fun queueRequestForRandomDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        val daysSinceFirstDate = ChronoUnit.DAYS.between(firstDate, homeDate)
        val randomDate = firstDate.plusDays(Random.nextLong(until = daysSinceFirstDate))

        queue.addStringRequest(
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
        queue.close()
    }
}