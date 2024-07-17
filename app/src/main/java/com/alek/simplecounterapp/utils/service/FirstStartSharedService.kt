package com.alek.simplecounterapp.utils.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * A simple class-singleton for exchanging events between two ViewModels
 * */
object FirstStartSharedService {

    private var isFirstStart: Boolean = false
    private val isFirstStartLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getFirstStartLiveData(): LiveData<Boolean> = isFirstStartLiveData

    fun setFirstStart(isFirstStart: Boolean) {
        this.isFirstStart = isFirstStart
        isFirstStartLiveData.value = isFirstStart
    }

    fun isFirstStart(): Boolean = isFirstStart
}