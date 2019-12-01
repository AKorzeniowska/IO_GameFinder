package com.example.ioagh.gamefinder.providers

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val sdfDateFromString = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.US)
val sdfStringFromDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
val calendar = GregorianCalendar.getInstance()!!

fun parseStringToDate(dateText: String): Date{
    return sdfDateFromString.parse(dateText)
}

fun parseDateToString(date: Date): String{
    return sdfDateFromString.format(date)
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
    return sdfStringFromDate.format(date)
}