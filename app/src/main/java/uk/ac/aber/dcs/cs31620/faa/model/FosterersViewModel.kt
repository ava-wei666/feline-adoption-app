package uk.ac.aber.dcs.cs31620.faa.model

import androidx.lifecycle.asLiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.map

class FosterersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    private val allFosterers = repository.fostererList.asLiveData()

    private val _searchDistance = MutableLiveData(50.0f)
    val searchDistance: LiveData<Float> = _searchDistance

    private val _searchRegion = MutableLiveData("Any region")
    val searchRegion: LiveData<String> = _searchRegion

    private val _currentUserLocation = MutableLiveData<Adopter?>(null)

    val fostererList: LiveData<List<Fosterer>> = allFosterers.switchMap { f ->
        _searchDistance.switchMap { dist ->
            _searchRegion.switchMap { reg ->
                _currentUserLocation.map { user ->
                    filterData(f, user, dist, reg)
                }
            }
        }
    }

    fun updateDistance(newDistance: Float) {
        _searchDistance.value = newDistance
    }

    fun updateRegion(newRegion: String) {
        _searchRegion.value = newRegion
    }

    fun updateUserLocation(user: Adopter?) {
        _currentUserLocation.value = user
    }
    private fun filterData(list: List<Fosterer>, user: Adopter?, maxDistanceKm: Float, targetRegion: String): List<Fosterer> {
        return list.filter { f ->
            val regionMatch = if (targetRegion == "Any region") true else f.regionName.equals(targetRegion, ignoreCase = true)

            // Only apply the distance filter when a user is logged in
            val distanceMatch = if (user == null) {
                true
            } else {
                val res = FloatArray(1)
                Location.distanceBetween(user.latitude, user.longitude, f.latitude, f.longitude, res)
                (res[0] / 1000) <= maxDistanceKm
            }

            regionMatch && distanceMatch
        }
    }

    fun getCatsForFosterer(fostererId: Long) = repository.getCatsByFosterer(fostererId).asLiveData()

    fun getFosterer(id: Long): LiveData<Fosterer?> {
        return repository.getFostererById(id).asLiveData()
    }
}