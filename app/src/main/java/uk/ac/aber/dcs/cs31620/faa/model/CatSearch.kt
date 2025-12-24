package uk.ac.aber.dcs.cs31620.faa.model

const val DEFAULT_DISTANCE = 50

data class CatSearch(
    var breed: String = "Any breed",
    var gender: String = "Any gender",
    var ageRange: String = "Any age",
    var distance: Int = DEFAULT_DISTANCE,
    val region: String = "Any region"
)
