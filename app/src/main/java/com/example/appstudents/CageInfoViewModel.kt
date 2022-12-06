package com.example.appstudents

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstudents.MyConstants.TAG
import com.example.appstudents.data.Cage
import com.example.appstudents.data.Student
import com.example.appstudents.repository.CagesRepository
import com.example.appstudents.repository.StudentsRepository
import java.util.*

class CageInfoViewModel : ViewModel() {
    var cage : MutableLiveData<Cage> = MutableLiveData()
    init {
        CagesRepository.getInstance().cage.observeForever{
            cage.postValue(it)
            Log.d(TAG, "Получили Cage в CageInfoViewModel")
        }
    }

    fun save(lastName: String="",
             firstName: String="",
             middleName: String="",
             birthDate : Date = Date(),
             faculty : String="",
             group : String=""){
        if (cage.value == null) cage.value= Cage()
        cage.value!!.lastName=lastName
        cage.value!!.firstName=firstName
        cage.value!!.middleName=middleName
        cage.value!!.birthDate=birthDate
        cage.value!!.faculty=faculty
        cage.value!!.group=group
        CagesRepository.getInstance().updateCage(cage.value!!)
    }
}