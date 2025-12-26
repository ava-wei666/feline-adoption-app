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

    private val _searchDistance = MutableLiveData(50.0f)
    val searchDistance: LiveData<Float> = _searchDistance

    private val _searchRegion = MutableLiveData("Any region")
    val searchRegion: LiveData<String> = _searchRegion

    private val _currentUserLocation = MutableLiveData<Adopter?>(null)

    // ✅ 修改点 1：简化逻辑链条，统一交给 filterData 处理
    val fostererList: LiveData<List<Fosterer>> = allFosterers.switchMap { fosterers ->
        _searchDistance.switchMap { distance ->
            _searchRegion.switchMap { region ->
                _currentUserLocation.map { user ->
                    // 无论有没有用户，都去执行过滤，让区域筛选始终有效
                    filterData(fosterers, user, distance, region)
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

    // ✅ 修改点 2：允许 user 为空 (Adopter?)
    private fun filterData(
        list: List<Fosterer>,
        user: Adopter?,
        maxDistanceKm: Float,
        targetRegion: String
    ): List<Fosterer> {
        return list.filter { fosterer ->
            // 1. 区域筛选 (始终生效)
            val isRegionOk = if (targetRegion == "Any region") {
                true
            } else {
                fosterer.regionName.equals(targetRegion, ignoreCase = true)
            }

            // 2. 距离筛选 (仅登录后生效)
            val isDistanceOk = if (user == null) {
                true // 游客状态，不卡距离
            } else {
                val results = FloatArray(1)
                Location.distanceBetween(
                    user.latitude, user.longitude,
                    fosterer.latitude, fosterer.longitude,
                    results
                )
                val distanceInKm = results[0] / 1000
                distanceInKm <= maxDistanceKm
            }

            isRegionOk && isDistanceOk
        }
    }

    // --- 下面你的代码保持不变 ---
    fun getCatsForFosterer(fostererId: Long) = repository.getCatsByFosterer(fostererId).asLiveData()

    fun getFosterer(id: Long): LiveData<Fosterer?> {
        return repository.getFostererById(id).asLiveData()
    }
}