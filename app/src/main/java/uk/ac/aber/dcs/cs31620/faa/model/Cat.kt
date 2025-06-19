package uk.ac.aber.dcs.cs31620.faa.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Represents a Cat
 * @author Chris Loftus
 */
data class Cat(val name: String,
               val gender: Gender,
               val breed: String,
               val description: String,
               val resourceId: Int,
               val dob: LocalDate
) {

    fun isKitten(): Boolean {
        val today = LocalDate.now()
        val fromDays = dob.until(today, ChronoUnit.DAYS)

        return (fromDays < 365 )
    }
}
