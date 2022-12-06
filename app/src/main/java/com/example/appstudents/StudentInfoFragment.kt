package com.example.appstudents

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.example.appstudents.data.Student
import java.util.*
// Панель работы с данными элемента
class StudentInfoFragment : Fragment() {
    private lateinit var studentInfoViewModel: StudentInfoViewModel
    private lateinit var name : EditText
    private lateinit var order : EditText
    private lateinit var family : EditText
    private lateinit var dpDate : DatePicker
    private lateinit var btnSave : Button
    private lateinit var btnCancel : Button

    companion object {
        private var INSTANCE: StudentInfoFragment? = null
        fun getInstance():StudentInfoFragment {
            if (INSTANCE == null) {
                INSTANCE = StudentInfoFragment()
            }
            return INSTANCE?: throw IllegalStateException("Отображение не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.student_info, container, false)
        name=view.findViewById(R.id.label)
        order=view.findViewById(R.id.order)
        family=view.findViewById(R.id.family)
        dpDate=view.findViewById(R.id.datePicker)
        btnSave=view.findViewById(R.id.btOk)
        btnSave.setOnClickListener {
            saveStudent()
            closeFragment()
        }
        btnCancel=view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
           closeFragment()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentInfoViewModel = ViewModelProvider(this).get(StudentInfoViewModel::class.java)
        studentInfoViewModel.student.observe(viewLifecycleOwner){
            updateUI(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true)
            {
                override fun handleOnBackPressed() {
                    closeFragment()
                    // Leave empty do disable back press or
                    // write your code which you want
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }


    fun saveStudent(){
        val dateBirth = GregorianCalendar(dpDate.year,  dpDate.month, dpDate.dayOfMonth)
        studentInfoViewModel.save(
            name.text.toString(),
            order.text.toString(),
            family.text.toString(),
            dateBirth.time,
        )
    }
    fun updateUI(student: Student){
        name.setText(student.name)
        order.setText(student.order)
        family.setText(student.family)
        val dateBirth = GregorianCalendar()
        dateBirth.time=student.birthDate
        dpDate.updateDate(dateBirth.get(Calendar.YEAR),dateBirth.get(Calendar.MONTH),dateBirth.get(Calendar.DAY_OF_MONTH))
    }

    private fun closeFragment(){
        (requireActivity() as MainActivity).showStudentsList()
    }

}