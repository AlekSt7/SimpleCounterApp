package com.alek.simplecounterapp.di

import com.alek.simplecounterapp.ui.activity.main_activity.MainActivityViewModelFactory
import com.alek.simplecounterapp.ui.fragments.start_fragment.StartFragmentViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [SharedModule::class])
class ViewModelModule {

    @Provides
    fun provideMainActivityViewModelFactory(factory: MainActivityViewModelFactory) = factory

    @Provides
    fun provideStartFragmentViewModelFactory(factory: StartFragmentViewModelFactory) = factory
}