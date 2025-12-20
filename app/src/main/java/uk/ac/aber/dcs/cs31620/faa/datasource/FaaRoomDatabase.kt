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
import uk.ac.aber.dcs.cs31620.faa.model.FostererDao //import DAO
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer //import entities

@Database(entities = [Cat::class, Fosterer::class], version = 2)
@TypeConverters(LocalDateTimeConverter::class, GenderConverter::class)
abstract class FaaRoomDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun fostererDao(): FostererDao

    companion object {
        private var instance: FaaRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): FaaRoomDatabase? {
            if (instance == null) {
                instance =
                    Room.databaseBuilder<FaaRoomDatabase>(
                        context.applicationContext,
                        FaaRoomDatabase::class.java,
                        "faa_database"
                    )
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))

                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {

                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: FaaRoomDatabase) {
            val upToOneYear = LocalDateTime.now().minusDays(365 / 2)
            val from1to2Years = LocalDateTime.now().minusDays(365 + (36 / 2))
            val from2to5Years = LocalDateTime.now().minusDays(365 * 3)
            val over5Years = LocalDateTime.now().minusDays(365 * 10)
            val admissionsDate = LocalDateTime.now().minusDays(60)
            val veryRecentAdmission = LocalDateTime.now()

            val upToOneYearCat = Cat(
                0, "Tibs", Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor...",
                upToOneYear,
                veryRecentAdmission,
                "file:///android_asset/images/cat1.png"
            )

            val from1to2YearsCat = Cat(
                0,
                "Tibs",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                from1to2Years,
                admissionsDate,
                "file:///android_asset/images/cat1.png"
            )

            val from2to5YearsCat = Cat(
                0,
                "Tibs",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                from2to5Years,
                admissionsDate,
                "file:///android_asset/images/cat1.png"
            )

            val over5YearsCat = Cat(
                0,
                "Tibs",
                Gender.MALE,
                "Moggie",
                "Lorem ipsum dolor sit amet, consectetur...",
                over5Years,
                admissionsDate,
                "file:///android_asset/images/cat1.png"
            )

            val catList = mutableListOf(
                upToOneYearCat,
                upToOneYearCat,
                from1to2YearsCat,
                from1to2YearsCat,
                from2to5YearsCat,
                from2to5YearsCat,
                over5YearsCat,
                over5YearsCat,
                over5YearsCat
            )

            val dao = instance.catDao()
            dao.insertMultipleCats(catList)

            // the new Fosterer data
            val fostererDao = instance.fostererDao()

            val fosterers = listOf(
                Fosterer(
                    name = "Alice Smith",
                    address = "123 Main St, Aberystwyth",
                    phoneNumber = "000000111",
                    latitude = 52.4913,
                    longitude = -4.0505,
                    regionName = "Bow Street"
                ),
                Fosterer(
                    name = "Bob Jones",
                    address = "Llanbadarn Fawr",
                    phoneNumber = "01970 654321",
                    latitude = 52.4100,
                    longitude = -4.0500,
                    regionName = "Aberystwyth"
                )
            )

            fostererDao.insertMultipleFosterers(fosterers)
        }
    }
}