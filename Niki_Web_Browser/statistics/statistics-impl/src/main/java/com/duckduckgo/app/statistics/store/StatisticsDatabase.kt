

package com.duckduckgo.app.statistics.store

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.duckduckgo.app.statistics.model.DailyPixelFired
import com.duckduckgo.app.statistics.model.UniquePixelFired
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Database(
    exportSchema = true,
    version = 1,
    entities = [
        DailyPixelFired::class,
        UniquePixelFired::class,
    ],
)
@TypeConverters(LocalDateConverter::class)
abstract class StatisticsDatabase : RoomDatabase() {
    abstract fun dailyPixelFiredDao(): DailyPixelFiredDao
    abstract fun uniquePixelFiredDao(): UniquePixelFiredDao
}

object LocalDateConverter {
    private val formatter: DateTimeFormatter
        get() = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? =
        localDate?.format(formatter)

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? =
        dateString?.let { LocalDate.parse(it, formatter) }
}
