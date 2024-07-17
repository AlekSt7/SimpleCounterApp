package com.alek.simplecounterapp.ui.fragments.start_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alek.simplecounterapp.utils.service.FirstStartSharedService
import javax.inject.Inject

class StartFragmentViewModelFactory @Inject constructor(
    private val firstStartSharedService: FirstStartSharedService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StartFragmentViewModel(firstStartSharedService) as T
}