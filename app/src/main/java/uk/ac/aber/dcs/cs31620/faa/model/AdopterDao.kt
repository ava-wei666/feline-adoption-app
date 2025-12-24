package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AdopterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdopter(adopter: Adopter)

    @Query("SELECT * FROM adopters WHERE username = :username AND password = :password")
    fun getAdopterByCredentials(username: String, password: String): Flow<Adopter?>

    @Query("SELECT * FROM adopters LIMIT 1")
    fun getAnyAdopter(): Flow<Adopter?>

    @Update
    suspend fun updateAdopter(adopter: Adopter)
}