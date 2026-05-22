package uk.ac.aber.dcs.cs31620.faa.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

/**
 * Unit tests for the [CatSearch] search-criteria model and the [Gender] enum.
 */
class CatSearchTest {

    @Test
    fun catSearch_appliesTheExpectedDefaults() {
        val search = CatSearch()
        assertEquals("Any breed", search.breed)
        assertEquals("Any gender", search.gender)
        assertEquals("Any age", search.ageRange)
        assertEquals("Any region", search.region)
        assertEquals(DEFAULT_DISTANCE, search.distance)
    }

    @Test
    fun catSearch_copyOverridesOnlyTheGivenField() {
        val search = CatSearch()
        val narrowed = search.copy(breed = "Bengal")
        assertEquals("Bengal", narrowed.breed)
        // Fields that were not overridden keep their defaults.
        assertEquals("Any gender", narrowed.gender)
        assertNotEquals(search, narrowed)
    }

    @Test
    fun catSearch_equalsComparesByValue() {
        assertEquals(CatSearch(), CatSearch())
        assertEquals(
            CatSearch(breed = "Birman", distance = 10),
            CatSearch(breed = "Birman", distance = 10)
        )
    }

    @Test
    fun gender_containsMaleAndFemaleOnly() {
        assertEquals(2, Gender.entries.size)
        assertEquals(Gender.MALE, Gender.valueOf("MALE"))
        assertEquals(Gender.FEMALE, Gender.valueOf("FEMALE"))
    }
}
