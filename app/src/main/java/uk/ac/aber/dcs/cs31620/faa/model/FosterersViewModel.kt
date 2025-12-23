package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository

class FosterersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    private val allFosterers = repository.fostererList.asLiveData()

    //filtering condition1: distance
    // _searchDistance for the FosterersViewModel using,
    //searchDistance for the UI, not clickable
    private val _searchDistance = MutableLiveData(50.0f)
    val searchDistance: LiveData<Float> = _searchDistance

    //filtering condition2: region
    private val _searchRegion = MutableLiveData("Any region")
    val searchRegion: LiveData<String> = _searchRegion

    private val _currentUserLocation = MutableLiveData<Adopter?>(null)

    val fostererList: LiveData<List<Fosterer>> = allFosterers.switchMap { fosterers ->
        _searchDistance.switchMap { distance ->
            _searchRegion.switchMap { region ->
                _currentUserLocation.map { user ->
                    if (user == null) {
                        fosterers
                    } else {
                        filterData(fosterers, user, distance, region)
                    }
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


    private fun filterData(
        list: List<Fosterer>,
        user: Adopter,
        maxDistanceKm: Float,
        targetRegion: String
    ): List<Fosterer> {
        return list.filter { fosterer ->
            //1.calculate the distance between fosterer and adopter
            val results = FloatArray(1)
            Location.distanceBetween(
                user.latitude, user.longitude,
                fosterer.latitude, fosterer.longitude,
                results
            )
            val distanceInKm = results[0] / 1000
            val isDistanceOk = distanceInKm <= maxDistanceKm

            val isRegionOk = if (targetRegion == "Any region") {
                true
            } else {
                fosterer.regionName.equals(targetRegion, ignoreCase = true)
            }

            isDistanceOk && isRegionOk
        }
    }

    fun getCatsForFosterer(fostererId: Long) = repository.getCatsByFosterer(fostererId).asLiveData()

    fun getFosterer(id: Long): Fosterer? {
        return allFosterers.value?.find { it.id == id }
    }
}