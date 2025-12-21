package uk.ac.aber.dcs.cs31620.faa.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import androidx.lifecycle.asLiveData

class FosterersViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FaaRepository(application)

    val fostererList: LiveData<List<Fosterer>> = repository.fostererList.asLiveData()

    //Obtain the corresponding cat based on the fostererId
    fun getCatsForFosterer(fostererId: Long): LiveData<List<Cat>>{
        return repository.getCatsByFosterer(fostererId).asLiveData()
    }

    //obtain the fosterer based on its id
    fun getFosterer(id: Long): Fosterer? {
        return fostererList.value?.find { it.id == id}
    }
}
