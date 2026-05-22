package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import androidx.lifecycle.*
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import android.location.Location
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

class CatsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    private val allCats = repository.getAllCats()
    private val allFosterers = repository.fostererList.asLiveData()

    var catSearch by mutableStateOf(CatSearch())
        private set

    private val _searchDistance = MutableLiveData(50.0f)
    val searchDistance: LiveData<Float> = _searchDistance

    val recentCats: LiveData<List<Cat>> = allCats.map { if (it.size > 5) it.take(5) else it }

    private val _currentUserLocation = MutableLiveData<Adopter?>(null)

    val catList: LiveData<List<Cat>> = allCats.switchMap { cats ->
        allFosterers.switchMap { fs ->
            _searchDistance.switchMap { d ->
                _currentUserLocation.map { u ->
                    cats.filter { c ->
                        val breedOk = catSearch.breed == "Any breed" || c.breed == catSearch.breed
                        val boss = fs.find { it.id == c.fostererId }
                        val regionOk = if (catSearch.region == "Any region") true else boss?.regionName?.trim()?.equals(catSearch.region.trim(), true) ?: false
                        breedOk && regionOk
                    }
                }
            }
        }
    }

    fun updateCatSearch(s: CatSearch) { catSearch = s; _searchDistance.value = _searchDistance.value }
    fun updateDistance(d: Float) { _searchDistance.value = d }
    fun updateUserLocation(u: Adopter?) { _currentUserLocation.value = u }

    fun insertCat(cat: Cat) { viewModelScope.launch { repository.insert(cat) } }
}