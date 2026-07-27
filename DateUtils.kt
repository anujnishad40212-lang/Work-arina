package com.taskflow.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun formatDate(millis: Long?): String {
        if (millis == null) return "No due date"
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(millis)
    }

    fun formatTodayLong(): String {
        val formatter = SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun isOverdue(millis: Long?, completed: Boolean): Boolean {
        if (millis == null || completed) return false
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return millis < today
    }

    fun greeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}