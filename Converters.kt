package com.taskflow.app.database

import androidx.room.TypeConverter
import com.taskflow.app.database.entity.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}