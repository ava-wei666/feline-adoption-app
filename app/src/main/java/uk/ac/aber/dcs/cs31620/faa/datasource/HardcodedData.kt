package uk.ac.aber.dcs.cs31620.faa.datasource

import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer
import uk.ac.aber.dcs.cs31620.faa.model.Gender

object HardcodedData {
    val initialFosterers = listOf(
        Fosterer(
            id = 1,
            name = "Shin Chan",
            address = "123 Main St",
            phoneNumber = "000000111",
            imageResId = R.drawable.shin_chan,
            latitude = 52.415, longitude = -4.082, regionName = "wales"
        ),
        Fosterer(
            id = 2,
            name = "Person2",
            address = "33 Pier St, Aberystwyth",
            phoneNumber = "07123456789",
            imageResId = R.drawable.person2,
            latitude = 52.416, longitude = -4.083, regionName = "wales"
        )
    )

    val initialCats = listOf(
        // cat assigned to shin chan
        Cat(id = 1, name = "Tibs", breed = "Moggie", gender = Gender.MALE, fostererId = 1, imagePath = "cat1.png"),

        // cat assigned to person2
        Cat(id = 2, name = "Tibs", breed = "Moggie", gender = Gender.FEMALE, fostererId = 2, imagePath = "cat1.png"),
        Cat(id = 3, name = "Tibs", breed = "Moggie", gender = Gender.MALE, fostererId = 2, imagePath = "cat1.png")
    )
}