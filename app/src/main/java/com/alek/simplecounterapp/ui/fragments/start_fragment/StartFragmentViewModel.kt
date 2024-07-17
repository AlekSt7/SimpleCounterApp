package com.alek.simplecounterapp.ui.fragments.start_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alek.simplecounterapp.utils.service.FirstStartSharedService

class StartFragmentViewModel(
    private val firstStartSharedService: FirstStartSharedService
) : ViewModel() {

    fun setFirstStart(isFirstStart: Boolean) {
        firstStartSharedService.setFirstStart(isFirstStart)
    }

    fun getFirstStartLiveData(): LiveData<Boolean> {
        Log.d("MainActivityViewModel", firstStartSharedService.toString())
        return firstStartSharedService.getFirstStartLiveData()
    }
}