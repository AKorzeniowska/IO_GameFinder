package com.example.ioagh.gamefinder.providers

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor
import kotlin.math.min

val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val calendar = GregorianCalendar.getInstance()!!

fun parseStringToDate(dateText: String): Date{
    return sdf.parse(dateText)
}

fun parseDateToString(date: Date): String{
    return sdf.format(date)
}

fun getHoursFromDate(date: Date): Int{
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun getMinutesFromDate(date: Date): Int{
    calendar.time = date
    return calendar.get(Calendar.MINUTE)
}

fun getOnlyDateFromDate(date: Date): String{
    return sdf.format(date)
}

fun parseMinutesToSring(minutes: Int): String{
    var output = ""
    val hours = floor((minutes/60).toDouble()).toInt()
    val remainingMinutes = minutes - hours*60
    return "$hours:$remainingMinutes"
}

fun parseStringToMinutes(string: String): Int{
    val hours = string.split(':')[0].toInt()
    val minutes = string.split(':')[1].toInt()
    return hours*60 + minutes
}

fun getTodaysDate(): Date{
    return Date()
}