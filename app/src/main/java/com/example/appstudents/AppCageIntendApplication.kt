package com.example.appstudents

import android.app.Application
import android.content.Context

class AppCageIntendApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppCageIntendApplication? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

}