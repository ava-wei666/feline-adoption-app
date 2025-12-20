package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface FostererDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleFosterers(fosterers: List<Fosterer>)

    @Query("SELECT * FROM fosterers")
    fun getAllFosterers(): Flow<List<Fosterer>>

    @Query("SELECT * FROM fosterers WHERE regionName = :region")
    fun getFosterersByRegion(region: String): Flow<List<Fosterer>>
}