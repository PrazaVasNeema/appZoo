package com.example.appstudents.data

import java.util.Date
import java.util.GregorianCalendar
import java.util.UUID

data class Student(
    val id : UUID =  UUID.randomUUID(),
    var name: String="",
    var order: String="",
    var family: String="",
    var birthDate : Date= Date(),
    ){


    val age : Int
       get(){
          val gregorianCalendar1 = GregorianCalendar()
           gregorianCalendar1.timeInMillis=birthDate.time
          val gregorianCalendar2 = GregorianCalendar()
          var y=gregorianCalendar2.get(GregorianCalendar.YEAR)-gregorianCalendar1.get(GregorianCalendar.YEAR)
          if (gregorianCalendar1.get(GregorianCalendar.MONTH)<gregorianCalendar2.get(GregorianCalendar.MONTH))
              y--
          return y
       }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Student

        if (id != other.id) return false
        if (name != other.name) return false
        if (order != other.order) return false
        if (family != other.family) return false
        if (birthDate != other.birthDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + order.hashCode()
        result = 31 * result + family.hashCode()
        result = 31 * result + birthDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Student(id=$id, lastName='$name', firstName='$order', middleName='$family', birthDate=$birthDate)"
    }

}