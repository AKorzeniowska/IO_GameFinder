package com.example.ioagh.gamefinder

import com.example.ioagh.gamefinder.providers.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import java.text.ParseException
import java.util.*


class DateTimeParserUnitTest {

    val cal = Calendar.getInstance()
    @Before
    fun setUp () {
        cal[Calendar.YEAR] = 1988
        cal[Calendar.MONTH] = Calendar.JANUARY
        cal[Calendar.DAY_OF_MONTH] = 1
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
    }

    @Test
    fun shouldParseSimpleStringToDate() {
        val dateRepresentation = cal.time
        assertEquals(parseStringToDate("1988-01-01 00:00"), dateRepresentation)
    }

    @Test(expected = ParseException::class)
    fun shouldThrowParseDateException() {
        parseStringToDate("1998-JAN-01 12:30")
    }

    @Test
    fun shouldParseDateToString() {
        val dateRepresentation = cal.time
        assertEquals(parseDateToString(dateRepresentation), "1988-01-01")
    }

    @Test
    fun shouldNotParseDateToStringWithSameFormat() {
        val dateRepresentation = cal.time
        assertNotEquals(parseDateToString(dateRepresentation), dateRepresentation.toString())
    }

    @Test
    fun shouldReturnHourEquals0FromDate() {
        val dateRepresentation = cal.time
        assertEquals(getHoursFromDate(dateRepresentation), 0)
    }

    @Test
    fun shouldGetMinutesEquals0FromDate() {
        val dateRepresentation = cal.time
        assertEquals(getMinutesFromDate(dateRepresentation), 0)
    }

    @Test
    fun shouldGetOnlyDateFromDateObject() {
        val dateRepresentation = cal.time
        assertEquals(getOnlyDateFromDate(dateRepresentation), "1988-01-01")
    }

    @Test
    fun shouldNotGetDateWithHoursFromDateObject() {
        val dateRepresentation = cal.time
        assertNotEquals(getOnlyDateFromDate(dateRepresentation), "1988-01-01 00:00")
    }

    @Test
    fun shouldParseMinutesToHoursAndMinutes() {
        val minutes = 200;
        assertEquals(parseMinutesToSring(minutes), "3:20")
    }

    @Test
    fun shouldParseStringToMinutes() {
        val minutes = "3:20"
        assertEquals(parseStringToMinutes(minutes), 200)
    }
}