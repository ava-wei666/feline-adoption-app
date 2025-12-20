package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.NonNull
// ... 导入其他 Room 相关类

@Entity(tableName = "fosterers")
data class Fosterer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val latitude: Double, // FR2 要求
    val longitude: Double, // FR2 要求
    val regionName: String // FR2 要求
)