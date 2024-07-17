package com.alek.simplecounterapp.ui.fragments.start_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alek.simplecounterapp.App
import com.alek.simplecounterapp.databinding.FragmentStartBinding
import javax.inject.Inject

class StartFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: StartFragmentViewModelFactory

    private val viewModel: StartFragmentViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[StartFragmentViewModel::class.java]
    }

    private val viewBinding: FragmentStartBinding by lazy {
        FragmentStartBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as App).appComponent.injectStartFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding.apply {

            viewModel.getFirstStartLiveData().observe(viewLifecycleOwner) {
                startButton.isEnabled = it
            }

            startButton.setOnClickListener {
                viewModel.setFirstStart(false)
            }
        }
        return viewBinding.root
    }
}