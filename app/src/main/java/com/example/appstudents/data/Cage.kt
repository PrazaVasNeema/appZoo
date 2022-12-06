package com.example.appstudents.data
//            TODO("toString, сравнение, хэшкод")
import java.util.Date
import java.util.GregorianCalendar
import java.util.UUID

data class Cage(
    val id : UUID =  UUID.randomUUID(),
    var label: String="",
    var students: MutableList<Student> = mutableListOf()
){


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cage

        if (id != other.id) return false
        if (label != other.label) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + label.hashCode()
        return result
    }

    override fun toString(): String {
        return "Student(id=$id, label='$label')"
    }

}