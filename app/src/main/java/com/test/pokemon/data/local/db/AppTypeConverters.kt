package com.test.pokemon.data.local.db

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class AppTypeConverters {
    private val moshi = Moshi.Builder().build()
    private val adapter = moshi.adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))

    @TypeConverter
    fun fromJson(value: String?): List<String> = value?.let { adapter.fromJson(it) } ?: emptyList()

    @TypeConverter
    fun toJson(list: List<String>): String = adapter.toJson(list)
}
