package uk.ac.aber.dcs.cs31620.faa.datasource.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import uk.ac.aber.dcs.cs31620.faa.model.Gender

/**
 * Unit tests for [GenderConverter], the Room type converter that maps a
 * [Gender] value to and from the string stored in the database.
 */
class GenderConverterTest {

    @Test
    fun toString_returnsTheEnumName() {
        assertEquals("MALE", GenderConverter.toString(Gender.MALE))
        assertEquals("FEMALE", GenderConverter.toString(Gender.FEMALE))
    }

    @Test
    fun toGender_parsesAValidName() {
        assertEquals(Gender.MALE, GenderConverter.toGender("MALE"))
        assertEquals(Gender.FEMALE, GenderConverter.toGender("FEMALE"))
    }

    @Test
    fun conversion_roundTripsForEveryGender() {
        for (gender in Gender.entries) {
            val restored = GenderConverter.toGender(GenderConverter.toString(gender))
            assertEquals(gender, restored)
        }
    }

    @Test
    fun toGender_throwsForAnUnknownName() {
        assertThrows(IllegalArgumentException::class.java) {
            GenderConverter.toGender("UNKNOWN")
        }
    }
}
