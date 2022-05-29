package com.mbahgojol.chami.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a")
        return sdf.format(Date())
    }

    fun reformatToClock(dateYMD: String): String {
        val dateYMDFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss a")
        try {
            val date = dateYMDFormat.parse(dateYMD)
            val dateFormat = SimpleDateFormat("hh:mm a")
            return dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
}