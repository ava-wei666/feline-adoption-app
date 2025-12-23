package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adopters")
data class Adopter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val password: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val imageResId: Int = uk.ac.aber.dcs.cs31620.faa.R.drawable.shin_chan
)