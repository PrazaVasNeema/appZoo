package com.example.appstudents

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstudents.MyConstants.TAG
import com.example.appstudents.data.Student
import com.example.appstudents.repository.StudentsRepository
import java.util.*
// Транзит для сохранения данных
class StudentInfoViewModel : ViewModel() {
    var student : MutableLiveData<Student> = MutableLiveData()
    init {
        StudentsRepository.getInstance().student.observeForever{
            student.postValue(it)
            Log.d(TAG, "Получили Student в StidentInfoViewModel")
        }
    }

    fun save(name: String="",
             order: String="",
             family: String="",
             birthDate : Date = Date()){
        if (student.value == null) student.value= Student()
        student.value!!.name=name
        student.value!!.order=order
        student.value!!.family=family
        student.value!!.birthDate=birthDate
        StudentsRepository.getInstance().updateStudent(student.value!!)
    }
}