package com.alek.simplecounterapp.di

import com.alek.simplecounterapp.utils.service.FirstStartSharedService
import dagger.Module
import dagger.Provides

@Module
class SharedModule {

    @Provides
    fun provideFirstStartSharedService(): FirstStartSharedService = FirstStartSharedService
}