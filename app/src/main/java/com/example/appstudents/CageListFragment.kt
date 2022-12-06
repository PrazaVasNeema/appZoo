package com.example.appstudents

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstudents.MyConstants.TAG

// Формирование списка элементов и взаимодействие с ним

class CageListFragment : Fragment() {
    private lateinit var cageListViewModel: CageListViewModel
    private lateinit var cageListRecyclerView: RecyclerView
    companion object {
        private var INSTANCE: CageListFragment? = null

        fun getInstance():CageListFragment {
            if (INSTANCE == null) {
                INSTANCE = CageListFragment()
            }
            return INSTANCE?: throw IllegalStateException("Отображение списка не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.list_of_cages, container, false)
        cageListRecyclerView=layoutView.findViewById(R.id.rvList)
        cageListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cageListViewModel = ViewModelProvider(this).get(StudentListViewModel::class.java)
        cageListViewModel.studentsList.observe(viewLifecycleOwner){
            updateUI(it)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        updateUI(studentListViewModel.studentsList.value)
//        Log.d(MyConstants.TAG, "StidentListFragment onResume")
//    }

    private inner class StudentsListAdapter(private val items: List<Student>)
        : RecyclerView.Adapter<StudentHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): StudentHolder {
            Log.d(MyConstants.TAG, "onCreateViewHolder1")
            val view = layoutInflater.inflate(R.layout.student_list_ement, parent, false)
            Log.d(MyConstants.TAG, "onCreateViewHolder2")
            return StudentHolder(view)
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: StudentHolder, position: Int) {
            Log.d(MyConstants.TAG, "onBindViewHolder 1 $position")
            holder.bind(items[position])
            Log.d(MyConstants.TAG, "onBindViewHolder 2 $position")
        }
    }

    private inner class StudentHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{
        private lateinit var student: Student
        private val fioTextView: TextView = itemView.findViewById(R.id.tvFIO)
        private val ageTextView: TextView = itemView.findViewById(R.id.tvAge)
        private val groupTextView: TextView = itemView.findViewById(R.id.tvGroup)
        private val clLayout: ConstraintLayout = itemView.findViewById(R.id.clCL)

        fun bind(student: Student) {
            Log.d(MyConstants.TAG, "bind 1 $student")
            this.student=student
            clLayout.setBackgroundColor(context!!.getColor(R.color.white))
            if (student.id==studentListViewModel.student.id)
                clLayout.setBackgroundColor(context!!.getColor(R.color.element))
            fioTextView.text="${student.lastName} ${student.firstName} ${student.middleName}"
            groupTextView.text=student.group
            ageTextView.text=student.age.toString()
            Log.d(MyConstants.TAG, "bind 2 $student")
        }

        init {
            itemView.setOnClickListener(this)
//!            itemView.setOnLongClickListener(this)
        }

        //!        override fun onClick(v: View?) {
//            Log.d(TAG, "StudentHolder onClick")
//            studentListViewModel.setStudent(student)
////            studentListRecyclerView.adapter = StudentsListAdapter(studentListViewModel.studentsList.value!!.items)
//            updateUI(studentListViewModel.studentsList.value)
////            (requireActivity() as MainActivity).checkDelete()
//            (requireActivity() as MainActivity).showStudentInfo()
//        }
        override fun onClick(v: View?) {
            Log.d(TAG, "StudentHolder onClick")
            studentListViewModel.setStudent(student)
            studentListRecyclerView.adapter = StudentsListAdapter(studentListViewModel.studentsList.value!!.items)
        }

//!        override fun onLongClick(v: View?): Boolean {
//            TODO("Not yet implemented")
//            studentListViewModel.setStudent(student)
//            (requireActivity() as MainActivity).checkDelete(student)
//            return true
//        }
    }

    private fun updateUI(studentsList: StudentsList? = null){
        if (studentsList==null) return
        //  Log.d(TAG, "`StidentListFragment updateUI $studentsList")
        studentListRecyclerView.layoutManager?.scrollToPosition(studentListViewModel.getPosition())
        studentListRecyclerView.adapter = StudentsListAdapter(studentsList.items)
    }

}