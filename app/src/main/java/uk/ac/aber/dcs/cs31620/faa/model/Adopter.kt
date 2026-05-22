package uk.ac.aber.dcs.cs31620.faa.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adopters")
data class Adopter(
    @PrimaryKey
    val username: String,
    val password: String,
    var name: String = "",
    var address: String = "",
    var phoneNumber: String = "",
    var region: String = "Any region",
    var latitude: Double = 52.41,
    var longitude: Double = -4.08,
    var imageResId: Int = 0
)