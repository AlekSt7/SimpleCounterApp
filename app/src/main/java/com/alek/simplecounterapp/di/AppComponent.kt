package com.alek.simplecounterapp.di

import com.alek.simplecounterapp.ui.activity.main_activity.MainActivity
import com.alek.simplecounterapp.ui.fragments.start_fragment.StartFragment
import dagger.Component

@Component(modules = [SharedModule::class])
interface AppComponent {

    fun injectMainActivity(mainActivity: MainActivity)

    fun injectStartFragment(startFragment: StartFragment)
}