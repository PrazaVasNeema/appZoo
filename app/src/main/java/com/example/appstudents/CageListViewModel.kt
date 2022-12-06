package com.example.appstudents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.appstudents.MyConstants.TAG
import com.example.appstudents.data.Cage
import com.example.appstudents.data.CagesList
import com.example.appstudents.data.Student
import com.example.appstudents.data.StudentsList
import com.example.appstudents.repository.CagesRepository
import com.example.appstudents.repository.StudentsRepository

class CageListViewModel : ViewModel() {
    var cagesList : MutableLiveData<CagesList> = MutableLiveData()
    private var cageCurrent : Cage = Cage()
    val cage : Cage
        get() = cageCurrent

    private var observer = Observer<CagesList?>
    { newList ->
        newList?.let {
            Log.d(TAG, "Получен список CageListViewModel от CagesRepository")
            cagesList.postValue(newList)
        }
    }

    init {
        CagesRepository.getInstance().cage.observeForever{
            cageCurrent=it
            Log.d(TAG, "Получили Cage в CageListViewModel")
        }
        CagesRepository.getInstance().cagesList.observeForever(observer)
        Log.d(TAG, "Подписались CageListViewModel к CagesRepository")
    }

    public fun setCage(cage: Cage){
        CagesRepository.getInstance().setCurrentCage(cage)
    }

    fun getPosition() : Int =
        CagesRepository.getInstance().getPosition()
}