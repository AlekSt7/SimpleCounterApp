package com.alek.simplecounterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alek.simplecounterapp.R
import com.alek.simplecounterapp.databinding.FragmentNativeCountersBinding

class NativeCountersFragment : Fragment() {

    private val viewBinding: FragmentNativeCountersBinding by lazy {
        FragmentNativeCountersBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

}