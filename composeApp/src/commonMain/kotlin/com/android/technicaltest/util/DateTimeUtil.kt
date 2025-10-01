package com.android.technicaltest.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object DateTimeUtil {
    fun formatRelativeTime(instant: Instant): String {
        val now = Clock.System.now()
        val diff = now.minus(instant).inWholeSeconds

        return when {
            diff < 60 -> "Just now"
            diff < 3600 -> "${diff / 60} minutes ago"
            diff < 86400 -> "${diff / 3600} hours ago"
            diff < 2592000 -> "${diff / 86400} days ago"
            else -> formatDateTime(instant)
        }
    }

    fun formatDateTime(instant: Instant): String {
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dateTime.year}-${dateTime.month.number.toString().padStart(2, '0')}-${dateTime.day.toString().padStart(2, '0')} " +
                "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
    }
}