package com.alek.simplecounterapp.ui.activity.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alek.simplecounterapp.utils.service.FirstStartSharedService

class MainActivityViewModel(
    private val firstStartSharedService: FirstStartSharedService
) : ViewModel() {

    fun getFirstStartLiveData(): LiveData<Boolean> {
        return firstStartSharedService.getFirstStartLiveData()
    }
}