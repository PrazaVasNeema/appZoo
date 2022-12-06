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
import com.example.appstudents.data.Cage
import java.util.*

class CageInfoFragment : Fragment(){
    private lateinit var cageInfoViewModel: CageInfoViewModel
    private lateinit var etLastName : EditText
    private lateinit var etFirstName : EditText
    private lateinit var etMiddleName : EditText
    private lateinit var etFaculty : EditText
    private lateinit var etGroup : EditText
    private lateinit var dpDate : DatePicker
    private lateinit var btnSave : Button
    private lateinit var btnCancel : Button

    companion object {
        private var INSTANCE: CageInfoFragment? = null
        fun getInstance():CageInfoFragment {
            if (INSTANCE == null) {
                INSTANCE = CageInfoFragment()
            }
            return INSTANCE?: throw IllegalStateException("Отображение не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.cage_info, container, false)
        etLastName=view.findViewById(R.id.lastName)
        etFirstName=view.findViewById(R.id.firstName)
        etMiddleName=view.findViewById(R.id.middleName)
        etFaculty=view.findViewById(R.id.faculty)
        etGroup=view.findViewById(R.id.group)
        dpDate=view.findViewById(R.id.datePicker)
        btnSave=view.findViewById(R.id.btOk)
        btnSave.setOnClickListener {
            saveCage()
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
        cageInfoViewModel = ViewModelProvider(this).get(CageInfoViewModel::class.java)
        cageInfoViewModel.cage.observe(viewLifecycleOwner){
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


    fun saveCage(){
        val dateBirth = GregorianCalendar(dpDate.year,  dpDate.month, dpDate.dayOfMonth)
        cageInfoViewModel.save(
            etLastName.text.toString(),
            etFirstName.text.toString(),
            etMiddleName.text.toString(),
            dateBirth.time,
            etFaculty.text.toString(),
            etGroup.text.toString()
        )
    }
    fun updateUI(cage: Cage){
        etLastName.setText(cage.lastName)
        etFirstName.setText(cage.firstName)
        etMiddleName.setText(cage.middleName)
        val dateBirth = GregorianCalendar()
        dateBirth.time=cage.birthDate
        dpDate.updateDate(dateBirth.get(Calendar.YEAR),dateBirth.get(Calendar.MONTH),dateBirth.get(
            Calendar.DAY_OF_MONTH))
        etFaculty.setText(cage.faculty)
        etGroup.setText(cage.group)
    }

    private fun closeFragment(){
        (requireActivity() as MainActivity).showCagesList()
    }
}