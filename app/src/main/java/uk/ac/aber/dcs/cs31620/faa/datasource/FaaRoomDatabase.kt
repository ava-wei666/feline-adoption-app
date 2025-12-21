package uk.ac.aber.dcs.cs31620.faa.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.datasource.util.GenderConverter
import uk.ac.aber.dcs.cs31620.faa.datasource.util.LocalDateTimeConverter
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatDao
import uk.ac.aber.dcs.cs31620.faa.model.Gender
import java.time.LocalDateTime
import uk.ac.aber.dcs.cs31620.faa.model.FostererDao
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer
import uk.ac.aber.dcs.cs31620.faa.R

@Database(entities = [Cat::class, Fosterer::class], version = 6)
@TypeConverters(LocalDateTimeConverter::class, GenderConverter::class)
abstract class FaaRoomDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun fostererDao(): FostererDao

    companion object {
        @Volatile
        private var instance: FaaRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): FaaRoomDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder<FaaRoomDatabase>(
                        context.applicationContext,
                        FaaRoomDatabase::class.java,
                        "faa_database"
                    )
                        .addCallback(roomDatabaseCallback(context))
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    coroutineScope.launch {
                        instance?.let {
                            populateDatabase(context, it)
                        }
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: FaaRoomDatabase) {
            val upToOneYear = LocalDateTime.now().minusDays(180)
            val from1to2Years = LocalDateTime.now().minusDays(540)
            val from2to5Years = LocalDateTime.now().minusDays(1095)
            val over5Years = LocalDateTime.now().minusDays(3650)
            val admissionsDate = LocalDateTime.now().minusDays(60)
            val veryRecentAdmission = LocalDateTime.now()

            val upToOneYearCat = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "Lorem ipsum...",
                upToOneYear, veryRecentAdmission, "file:///android_asset/images/cat1.png"
            )

            val from1to2YearsCat = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "Lorem ipsum...",
                from1to2Years, admissionsDate, "file:///android_asset/images/cat1.png"
            )

            val from2to5YearsCat = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "Lorem ipsum...",
                from2to5Years, admissionsDate, "file:///android_asset/images/cat1.png"
            )

            val over5YearsCat = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "Lorem ipsum...",
                over5Years, admissionsDate, "file:///android_asset/images/cat1.png"
            )

            val catList = mutableListOf(
                upToOneYearCat, from1to2YearsCat, from2to5YearsCat, over5YearsCat
            )

            instance.catDao().insertMultipleCats(catList)

            val fosterers = listOf(
                Fosterer(
                    name = "Shin Chan", address = "123 Main St", phoneNumber = "000000111",
                    latitude = 52.4913, longitude = -4.0505, regionName = "Bow Street",
                    imageResId = R.drawable.shin_chan
                ),
                Fosterer(
                    name = "Person2", address = "Llanbadarn Fawr", phoneNumber = "01970 654321",
                    latitude = 52.4100, longitude = -4.0500, regionName = "Aberystwyth",
                    imageResId = R.drawable.person2
                )
            )

            instance.fostererDao().insertMultipleFosterers(fosterers)
        }
    }
}