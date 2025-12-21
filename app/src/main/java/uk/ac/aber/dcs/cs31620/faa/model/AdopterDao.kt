package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AdopterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdopter(adopter: Adopter)

    // 登录查询：根据用户名和密码找人
    @Query("SELECT * FROM adopters WHERE username = :username AND password = :password")
    fun getAdopterByCredentials(username: String, password: String): Flow<Adopter?>

    // 获取当前登录用户（这里简化，假设只取第一个，或者用于调试）
    @Query("SELECT * FROM adopters LIMIT 1")
    fun getAnyAdopter(): Flow<Adopter?>
}