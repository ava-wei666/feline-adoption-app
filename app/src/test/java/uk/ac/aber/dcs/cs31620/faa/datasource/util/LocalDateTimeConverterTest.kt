package uk.ac.aber.dcs.cs31620.faa.datasource.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

/**
 * Unit tests for [LocalDateTimeConverter], the Room type converter that maps a
 * [LocalDateTime] to and from an epoch-millisecond timestamp.
 */
class LocalDateTimeConverterTest {

    @Test
    fun conversion_roundTripsToTheSameDateTime() {
        val original = LocalDateTime.of(2024, 3, 15, 10, 30, 0)
        val timestamp = LocalDateTimeConverter.toTimestamp(original)
        val restored = LocalDateTimeConverter.toLocalDate(timestamp)
        assertEquals(original, restored)
    }

    @Test
    fun toTimestamp_increasesAsTheDateTimeAdvances() {
        val earlier = LocalDateTime.of(2024, 1, 1, 0, 0, 0)
        val later = LocalDateTime.of(2024, 12, 31, 23, 59, 59)
        assertTrue(
            LocalDateTimeConverter.toTimestamp(earlier) < LocalDateTimeConverter.toTimestamp(later)
        )
    }

    @Test
    fun toLocalDate_recoversTheOriginalCalendarFields() {
        val timestamp = LocalDateTimeConverter.toTimestamp(LocalDateTime.of(2020, 6, 1, 12, 0, 0))
        val restored = LocalDateTimeConverter.toLocalDate(timestamp)
        assertEquals(2020, restored.year)
        assertEquals(6, restored.monthValue)
        assertEquals(1, restored.dayOfMonth)
    }
}
