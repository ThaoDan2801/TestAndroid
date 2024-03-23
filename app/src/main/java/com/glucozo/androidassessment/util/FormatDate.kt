package com.glucozo.androidassessment.util

import java.text.SimpleDateFormat
import java.util.Date

object FormatDate {
        private const val DATE = "dd/MM/yyyy"
    fun convertTimestampToString(epochTimestamp: Long): String {
        val date = Date(epochTimestamp * 1000)
        val format = SimpleDateFormat(DATE)
        return format.format(date)
    }
}