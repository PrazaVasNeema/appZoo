package com.example.appstudents

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import com.example.appstudents.data.Cage
import java.util.*

class CageInfoFragment : Fragment(){
    private lateinit var cageInfoViewModel: CageInfoViewModel
    private lateinit var label : EditText
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
        label=view.findViewById(R.id.label)
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
        cageInfoViewModel.save(
            label.text.toString(),
        )
    }
    fun updateUI(cage: Cage){
        label.setText(cage.label)
    }

    private fun closeFragment(){
        (requireActivity() as MainActivity).showCagesList()
    }
}