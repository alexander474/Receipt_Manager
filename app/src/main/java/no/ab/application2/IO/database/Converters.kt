package no.ab.application2.IO.database

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Inspiration from http://danielgaribaldi.com/room-persistence-library-kotlin/ (03.04.2019)
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {

        return when (value) {
            null -> null
            else -> Date(value)
        }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {

        return when (date) {
            null -> null
            else -> date.time
        }
    }
}