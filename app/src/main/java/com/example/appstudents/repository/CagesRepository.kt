package com.example.appstudents.repository

import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.appstudents.AppStudentIntendApplication
import com.example.appstudents.data.Student
import com.example.appstudents.data.StudentsList
import com.example.appstudents.AppCageIntendApplication
import com.example.appstudents.data.Cage
import com.example.appstudents.data.CagesList
import com.google.gson.Gson


class CagesRepository {

    companion object {
        private var INSTANCE: CagesRepository? = null

        fun getInstance(): CagesRepository {
            if (INSTANCE == null) {
                INSTANCE = CagesRepository()
            }
            return INSTANCE ?:
            throw IllegalStateException("Репозиторий CagesRepository не инициализирован")
        }
    }

    var cagesList: MutableLiveData<CagesList?> = MutableLiveData()
    var cage : MutableLiveData<Cage> = MutableLiveData()

    init {
        loadCages()
    }

    fun loadCages(){
        val jsonString=
            PreferenceManager.getDefaultSharedPreferences(AppCageIntendApplication.applicationContext())
                .getString("cages",null)
        if (!jsonString.isNullOrBlank()) {
            val st = Gson().fromJson(jsonString, CagesList::class.java)
            if (st!=null)
                this.cagesList.postValue(st)
        }
    }

    fun saveCages(){
        val gson = Gson()
        var jsonCages = gson.toJson(cagesList.value)
        val preference =
            PreferenceManager.getDefaultSharedPreferences(AppCageIntendApplication.applicationContext())
        preference.edit().apply {
            putString("students", jsonCages)
            apply()
        }
    }


    fun setCurrentCage(position: Int){
        if (cagesList.value==null || cagesList.value!!.items==null ||
            position<0 || (cagesList.value?.items?.size!! <= position))
            return
        cage.postValue(cagesList.value?.items!![position])
    }

    fun setCurrentCage(cage :Cage){
        this.cage.postValue(cage)
    }

    fun addCage(cage: Cage) {
        var cagesListTmp = cagesList.value
        if (cagesListTmp == null) cagesListTmp = CagesList()
        cagesListTmp.items.add(cage)
        cagesList.postValue(cagesListTmp)
    }

    fun getPosition(cage: Cage): Int = cagesList.value?.items?.indexOfFirst {
        it.id == cage.id } ?: -1

    fun getPosition() :Int
    {
        if (cage.value!=null)
        {
            return getPosition(cage.value!!)
        }
        else
            return 0
    }

    fun updateCage(cage: Cage) {
        val position = getPosition(cage)
        if (position < 0) addCage(cage)
        else {
            var cagesListTmp = cagesList
            cagesListTmp.value!!.items[position]=cage
            cagesList.postValue(cagesListTmp.value)
        }
    }

    fun deleteCage(cage: Cage){
        var cagesListTmp = cagesList
        if (cagesListTmp.value!!.items.remove(cage)) {
            cagesList.postValue(cagesListTmp.value)
        }
    }

    fun deleteCage(){
        if (cage.value!=null)
            deleteCage(cage.value!!)
    }

    fun newCage(){
        setCurrentCage(Cage())
    }

}