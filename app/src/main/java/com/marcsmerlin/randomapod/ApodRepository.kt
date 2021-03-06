package com.marcsmerlin.randomapod

import com.marcsmerlin.randomapod.utils.IStringQueue
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

/*
ApodRepository: class used to provide access to the NASA Apod archive at a predefined endpoint
with an assigned api_key. Requires the REST service of an IStringRequest queue.
 */
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

    fun queueHomeRequest(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        queue.addStringRequest(
            baseUrl,
            { string ->
                val apod = Apod(string)
                currentDate = apod.localDate
                homeDate = currentDate
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
        if (hasNextDate()) {
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
    }

    fun hasPreviousDate(): Boolean {
        return currentDate.isAfter(firstDate)
    }

    fun queueRequestForPreviousDate(
        apodListener: (Apod) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        if (hasPreviousDate()) {
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