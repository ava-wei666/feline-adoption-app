package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import androidx.lifecycle.asLiveData

class FosterersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    // use .asLiveData() to convert the Flow from DAO to LiveData for the UI
    //val fostererList: LiveData<List<Fosterer>> = repository.fostererList.asLiveData()

    //val fostererList: LiveData<List<Fosterer>> = repository.fostererList.asLiveData()
    val fostererList: LiveData<List<Fosterer>> = repository.fostererList.asLiveData()

    //val fostererList: LiveData<List<Fosterer>> = MutableLiveData(emptyList())
}
