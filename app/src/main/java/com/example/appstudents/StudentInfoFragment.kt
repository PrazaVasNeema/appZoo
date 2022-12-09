package com.example.appstudents

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appstudents.data.Student
import java.text.SimpleDateFormat
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
    private lateinit var tvDpError: TextView
    var errorFlag: Boolean = false

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
        tvDpError = view.findViewById(R.id.tvDPError)
        btnSave=view.findViewById(R.id.btOk)
        btnSave.setOnClickListener {
            tvDpError.error = null
            errorFlag = false
            saveStudent()
            if (!errorFlag) {
                closeFragment()
            }
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
        val nameRegex = "^[А-Яа-яA-Za-z-' ']+$"
        val currentTime = GregorianCalendar.getInstance().time
        val dateBirth = GregorianCalendar(dpDate.year,  dpDate.month, dpDate.dayOfMonth)

        if (!nameRegex.toRegex().matches(name.text)) {
            name.error = getString(R.string.animal_name_error)
            errorFlag = true
        }
        if (!nameRegex.toRegex().matches(order.text)) {
            order.error = getString(R.string.animal_order_error)
            errorFlag = true
        }
        if (!nameRegex.toRegex().matches(family.text)) {
            family.error = getString(R.string.animal_family_error)
            errorFlag = true
        }
        if (dpDate.year < 2000 || currentTime.before(dateBirth.time)) {
            tvDpError.requestFocus()
            tvDpError.error = getString(R.string.animal_date_error)
            errorFlag = true
        }
        if(!errorFlag) {
            studentInfoViewModel.save(
                name.text.toString(),
                order.text.toString(),
                family.text.toString(),
                dateBirth.time,
            )
        }
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