package no.uio.ifi.in2000_gruppe3.data.database

import androidx.room.TypeConverter

class Converter{

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return string.split(",")
    }
}