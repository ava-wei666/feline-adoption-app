package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "fosterers")
data class Fosterer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double,
    val regionName: String,
    val imageResId: Int= 0
)