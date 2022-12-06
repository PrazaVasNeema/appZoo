package com.example.appstudents.data

data class CagesList(
    val items : MutableList<Cage> = mutableListOf()
){
    override fun toString(): String {
        return "CagesList(items=$items)"
    }
}
