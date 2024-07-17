package com.alek.simplecounterapp.ui.activity.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alek.simplecounterapp.utils.service.FirstStartSharedService
import javax.inject.Inject

class MainActivityViewModelFactory @Inject constructor(
    private val firstStartSharedService: FirstStartSharedService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MainActivityViewModel(firstStartSharedService) as T
}