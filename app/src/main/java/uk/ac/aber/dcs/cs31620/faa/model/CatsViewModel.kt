package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository

class CatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    private val allCats: LiveData<List<Cat>> = repository.getAllCats()
    private val allFosterers: LiveData<List<Fosterer>> = repository.fostererList.asLiveData()

    var catSearch by mutableStateOf(CatSearch())
        private set

    private val _searchDistance = MutableLiveData(50.0f)
    val searchDistance: LiveData<Float> = _searchDistance

    private val _currentUserLocation = MutableLiveData<Adopter?>(null)

    val catList: LiveData<List<Cat>> = allCats.switchMap { cats: List<Cat> ->
        allFosterers.switchMap { fosterers: List<Fosterer> ->
            _searchDistance.switchMap { distance: Float ->
                _currentUserLocation.map { user: Adopter? ->
                    filterCats(cats, fosterers, user, distance, catSearch)
                }
            }
        }
    }


    val recentCats: LiveData<List<Cat>> = allCats.map { list: List<Cat> ->
        if (list.size > 5) list.take(5) else list
    }

    fun updateCatSearch(newSearch: CatSearch) {
        catSearch = newSearch
        _searchDistance.value = _searchDistance.value
    }

    fun updateDistance(dist: Float) { _searchDistance.value = dist }
    fun updateUserLocation(user: Adopter?) { _currentUserLocation.value = user }

    private fun filterCats(
        cats: List<Cat>,
        fosterers: List<Fosterer>,
        user: Adopter?,
        maxDistance: Float,
        search: CatSearch
    ): List<Cat> {
        return cats.filter { cat ->
            val breedMatch = search.breed == "Any breed" || cat.breed == search.breed
            val genderMatch = search.gender == "Any gender" || cat.gender.name.equals(search.gender, true)


            val fosterer = fosterers.find { it.id == cat.fostererId }

            val regionMatch = if (search.region == "Any region") true else {
                fosterer?.regionName.equals(search.region, true) ?: false
            }

            val distanceMatch = if (user == null || fosterer == null) true else {
                val results = FloatArray(1)
                Location.distanceBetween(user.latitude, user.longitude, fosterer.latitude, fosterer.longitude, results)
                (results[0] / 1000) <= maxDistance
            }

            breedMatch && genderMatch && regionMatch && distanceMatch
        }
    }

    fun insertCat(cat: Cat) {
        viewModelScope.launch {
            repository.insert(cat)
        }
    }
}