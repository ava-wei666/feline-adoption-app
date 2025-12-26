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

    // 保持你原有的 switchMap 链式结构
    val catList: LiveData<List<Cat>> = allCats.switchMap { cats: List<Cat> ->
        allFosterers.switchMap { fosterers: List<Fosterer> ->
            _searchDistance.switchMap { distance: Float ->
                _currentUserLocation.map { user: Adopter? ->
                    // 无论 user 是否为 null，都进入 filterCats 处理
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
        // 触发 LiveData 链式更新
        _searchDistance.value = _searchDistance.value
    }

    fun updateDistance(dist: Float) { _searchDistance.value = dist }
    fun updateUserLocation(user: Adopter?) { _currentUserLocation.value = user }

    // ✅ 核心修复：优化过滤逻辑以支持游客模式
    private fun filterCats(
        cats: List<Cat>,
        fosterers: List<Fosterer>,
        user: Adopter?,
        maxDistance: Float,
        search: CatSearch
    ): List<Cat> {
        return cats.filter { cat ->
            // 1. 基础属性筛选
            val breedMatch = search.breed == "Any breed" || cat.breed == search.breed
            val genderMatch = search.gender == "Any gender" || cat.gender.name.equals(search.gender, true)

            // 找到这只猫对应的主人
            val fosterer = fosterers.find { it.id == cat.fostererId }

            // 2. 区域筛选 (修正逻辑：即使没找到主人，只要选 Any region 也算匹配)
            val regionMatch = if (search.region == "Any region") {
                true
            } else {
                fosterer?.regionName.equals(search.region, true) ?: false
            }

            // 3. 距离筛选 (修正逻辑：如果是游客 user == null，则直接跳过距离检查，返回 true)
            val distanceMatch = if (user == null) {
                true // 游客状态下，不进行距离限制
            } else {
                // 已登录状态，尝试计算距离
                fosterer?.let {
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        user.latitude, user.longitude,
                        it.latitude, it.longitude,
                        results
                    )
                    (results[0] / 1000) <= maxDistance
                } ?: false // 如果找不到寄养人坐标，则不显示（安全考虑）
            }

            breedMatch && genderMatch && regionMatch && distanceMatch
        }
    }

    // 保持你原有的插入逻辑
    fun insertCat(cat: Cat) {
        viewModelScope.launch {
            repository.insert(cat)
        }
    }
}