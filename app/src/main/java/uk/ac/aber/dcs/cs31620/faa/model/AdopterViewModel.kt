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

    // used to store current login user
    private val _user = MutableLiveData<Adopter?>(null)
    val user: LiveData<Adopter?> = _user

    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            repository.login(username, password).collect { adopter ->
                _user.value = adopter
            }
        }
    }

    fun doLogout() {
        _user.value = null
    }

    fun isUserLoggedIn(): Boolean {
        return _user.value != null
    }

    fun saveUserChanges(updatedAdopter: Adopter) {
        viewModelScope.launch {
            repository.updateAdopter(updatedAdopter)
            _user.value = updatedAdopter
        }
    }
}