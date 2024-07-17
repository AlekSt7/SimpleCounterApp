package com.alek.simplecounterapp

import android.app.Application
import com.alek.simplecounterapp.di.AppComponent
import com.alek.simplecounterapp.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }
}