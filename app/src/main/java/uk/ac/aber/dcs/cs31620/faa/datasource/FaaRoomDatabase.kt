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
import uk.ac.aber.dcs.cs31620.faa.model.Adopter
import uk.ac.aber.dcs.cs31620.faa.model.AdopterDao

@Database(entities = [Cat::class, Fosterer::class, Adopter::class], version = 11)
@TypeConverters(LocalDateTimeConverter::class, GenderConverter::class)
abstract class FaaRoomDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
    abstract fun fostererDao(): FostererDao
    abstract fun adopterDao(): AdopterDao

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
            val now = LocalDateTime.now()

            val fosterers = listOf(
                Fosterer(
                    id = 1L, name = "Shin Chan", address = "123 Main St", phoneNumber = "000000111",
                    latitude = 52.4913, longitude = -4.0505, regionName = "Bow Street",
                    imageResId = R.drawable.shin_chan
                ),
                Fosterer(
                    id = 2L, name = "Person2", address = "Aberystwyth Town", phoneNumber = "01970 654321",
                    latitude = 52.4100, longitude = -4.0500, regionName = "Aberystwyth",
                    imageResId = R.drawable.person2
                )
            )
            instance.fostererDao().insertMultipleFosterers(fosterers)

            val cat1 = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "A cute cat",
                now.minusDays(180), now, "file:///android_asset/images/cat1.png",
                fostererId = 1L
            )

            val cat2 = Cat(
                0, "Tibs", Gender.FEMALE, "Moggie", "Another cat",
                now.minusDays(540), now.minusDays(60), "file:///android_asset/images/cat1.png",
                fostererId = 2L
            )

            val cat3 = Cat(
                0, "Tibs", Gender.MALE, "Moggie", "Third cat",
                now.minusDays(180), now.minusDays(60), "file:///android_asset/images/cat1.png",
                fostererId = 2L
            )

            instance.catDao().insertMultipleCats(listOf(cat1, cat2, cat3))

            val adopterDao = instance.adopterDao()
            adopterDao.insertAdopter(Adopter(
                username = "user", password = "password", name = "My Test User",
                address = "Aberystwyth University", latitude = 52.4180, longitude = -4.0657,
                imageResId = R.drawable.shin_chan
            ))
        }
    }
}