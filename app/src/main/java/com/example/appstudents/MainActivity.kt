package com.example.appstudents

import android.app.AlertDialog
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.appstudents.MyConstants.STUDENT_INFO_FRAGMENT_TAG
import com.example.appstudents.MyConstants.STUDENT_LIST_FRAGMENT_TAG
import com.example.appstudents.data.Student
import com.example.appstudents.repository.StudentsRepository

import com.example.appstudents.MyConstants.CAGE_INFO_FRAGMENT_TAG
import com.example.appstudents.MyConstants.CAGE_LIST_FRAGMENT_TAG
import com.example.appstudents.data.Cage
import com.example.appstudents.repository.CagesRepository


class MainActivity : AppCompatActivity() {

    private var miAdd : MenuItem? = null
    private var miDelete : MenuItem? = null
    private var miChange : MenuItem? = null
//    var cageUUID : String? = null
    var isCage : Boolean = true
    companion object {
        lateinit var cageUUID: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isCage = true
        showCagesList()
//        showStudentsList()
        val callback =  object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    checkLogout()
                }
            }
        onBackPressedDispatcher.addCallback(this,callback)

    }


 /*   override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        saveData()
        super.onSaveInstanceState(outState, outPersistentState)
    }
*/
    override fun onSaveInstanceState(outState: Bundle)
    {
        saveData()
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        loadData()
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    private fun loadData(){
        if(isCage){
            CagesRepository.getInstance().loadCages()
        }
        else{
            StudentsRepository.getInstance().loadStudents()
        }
     }

    private fun saveData(){
        if(isCage){
            CagesRepository.getInstance().saveCages()
        }
        else{
            StudentsRepository.getInstance().saveStudents()
        }
    }

    override fun onStop() {
        if(isCage) {
            CagesRepository.getInstance().saveCages()
        }
        else {
            CagesRepository.getInstance().saveCages()
            StudentsRepository.getInstance().saveStudents()
        }
//        saveData()
        super.onStop()
    }

    private fun checkLogout(){
        if(isCage == false)
        {
            isCage = true
            StudentsRepository.getInstance().saveStudents()
            StudentsRepository.getInstance().deInit()
            showCagesList()
        }
        else {
            AlertDialog.Builder(this)
                .setTitle("Выход!") // заголовок
                .setMessage("Вы действительно хотите выйти из приложения ?") // сообщение
                .setPositiveButton("ДА") { _, _ ->
                    finish()
                }
                .setNegativeButton("НЕТ", null)
                .setCancelable(true)
                .create()
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        miAdd=menu.findItem(R.id.miAdd)
        miDelete=menu.findItem(R.id.miDelete)
        miChange=menu.findItem(R.id.miChange)
        return true
    }

    fun checkDeleteCage(cage: Cage?=CagesRepository.getInstance().cage.value) {

        if (cage == null) return
        val s = cage.label
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!") // заголовок
            .setMessage("Вы действительно хотите удалить вольер $s ?") // сообщение
            .setPositiveButton("ДА") { _, _ ->
                CagesRepository.getInstance().deleteCage(cage) // ?
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()
    }

    fun cageDialogueWindow(cage: Cage?=CagesRepository.getInstance().cage.value)
    {
        if (cage == null) return
        val s = cage.label
        AlertDialog.Builder(this)
            .setTitle(cage.label) // заголовок
//            .setMessage("Вы действительно хотите удалить вольер $s ?") // сообщение
            .setPositiveButton("Изменить") { _, _ ->
                showCageInfo() // ?
            }
            .setNegativeButton("Удалить") { _, _ ->
                CagesRepository.getInstance().deleteCage(cage) // ?
            }
            .setNeutralButton("Отмена", null)
            .setCancelable(true)
            .create()
            .show()
    }

    fun checkDeleteStudent(student: Student?=StudentsRepository.getInstance().student.value){

        if(student == null) return
        val s=student.name+" "+
                student.order+" "+
                student.family
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!") // заголовок
            .setMessage("Вы действительно хотите удалить животное $s ?") // сообщение
            .setPositiveButton("ДА") { _ , _ ->
                StudentsRepository.getInstance().deleteStudent(student) // ?
            }
            .setNegativeButton("НЕТ", null)
            .setCancelable(true)
            .create()
            .show()


//        val s=StudentsRepository.getInstance().student.value?.lastName+" "+
//                StudentsRepository.getInstance().student.value?.firstName+" "+
//                StudentsRepository.getInstance().student.value?.middleName
//        AlertDialog.Builder(this)
//            .setTitle("УДАЛЕНИЕ!") // заголовок
//            .setMessage("Вы действительно хотите удалить студента $s ?") // сообщение
//            .setPositiveButton("ДА") { _ , _ ->
//                StudentsRepository.getInstance().deleteStudent()
//            }
//            .setNegativeButton("НЕТ", null)
//            .setCancelable(true)
//            .create()
//            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAdd -> {
                if(isCage){
                    showNewCage()
                }
                else{
                    showNewStudent()
                }
                true
            }
            R.id.miDelete -> {
                if(isCage){
                    checkDeleteCage()
                }
                else{
                    checkDeleteStudent()
                }
                true
            }
            R.id.miChange -> {
                if(isCage){
                    showCageInfo()
                }
                else{
                    showStudentInfo()
                }
                true
            }
            R.id.miEnter -> {
//                cageUUID = CagesRepository.getInstance().cage.value?.id.toString()
//                Log.d(MyConstants.TAG, "MainActivity UUID: $cageUUID")
//                isCage = false
//                showStudentsList()
                enterCage();
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun enterCage(cage: Cage?=CagesRepository.getInstance().cage.value)
    {
        if (cage == null) return
        cageUUID = cage.id.toString()
        Log.d(MyConstants.TAG, "MainActivity UUID: $cageUUID")
        isCage = false
        showStudentsList()
    }

    fun showNewCage(){
        CagesRepository.getInstance().newCage()
        showCageInfo()
    }

    fun showCagesList(){
        miAdd?.isVisible=true
        miDelete?.isVisible=true
        miChange?.isVisible=true
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, CageListFragment.getInstance(),CAGE_LIST_FRAGMENT_TAG)
            //.addToBackStack(null)
            .commit()
    }

    fun showCageInfo(){
        miAdd?.isVisible=false
        miDelete?.isVisible=false
        miChange?.isVisible=false
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, CageInfoFragment.getInstance(), CAGE_INFO_FRAGMENT_TAG)
            // .addToBackStack(null)
            .commit()
    }

    fun showNewStudent(){
        StudentsRepository.getInstance().newStudent()
        showStudentInfo()
    }

    fun showStudentsList(cage: Cage?=CagesRepository.getInstance().cage.value){
        miAdd?.isVisible=true
        miDelete?.isVisible=true
        miChange?.isVisible=true
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, StudentListFragment.getInstance(),STUDENT_LIST_FRAGMENT_TAG)
                //.addToBackStack(null)
                .commit()
    }

    fun showStudentInfo(){
        miAdd?.isVisible=false
        miDelete?.isVisible=false
        miChange?.isVisible=false
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, StudentInfoFragment.getInstance(), STUDENT_INFO_FRAGMENT_TAG)
           // .addToBackStack(null)
            .commit()
    }
}