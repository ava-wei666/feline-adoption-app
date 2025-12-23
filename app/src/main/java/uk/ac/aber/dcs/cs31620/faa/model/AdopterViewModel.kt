package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository

class AdopterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    // 用来存“当前登录的用户”。如果是 null，说明没登录。
    private val _user = MutableLiveData<Adopter?>(null)
    val user: LiveData<Adopter?> = _user

    // 登录动作
    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            // 去仓库查，并收集结果
            repository.login(username, password).collect { adopter ->
                // 如果查到了，就把这个用户存起来
                _user.value = adopter
            }
        }
    }

    // 登出动作
    fun doLogout() {
        _user.value = null
    }

    // 检查是否登录
    fun isUserLoggedIn(): Boolean {
        return _user.value != null
    }
}