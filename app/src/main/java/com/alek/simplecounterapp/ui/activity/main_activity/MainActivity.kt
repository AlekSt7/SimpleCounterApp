package com.alek.simplecounterapp.ui.activity.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alek.simplecounterapp.App
import com.alek.simplecounterapp.ui.view_pager.page_transformer.DepthPageTransformer
import com.alek.simplecounterapp.ui.view_pager.adapter.PagerFragmentAdapter
import com.alek.simplecounterapp.databinding.ActivityMainBinding
import com.alek.simplecounterapp.di.DaggerAppComponent
import com.alek.simplecounterapp.ui.fragments.custom_counters_fragment.CustomCountersFragment
import com.alek.simplecounterapp.ui.fragments.NativeCountersFragment
import com.alek.simplecounterapp.ui.fragments.start_fragment.StartFragment
import com.alek.simplecounterapp.utils.ViewUtils.setViewVisibility
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory

    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        (application as App).appComponent.injectMainActivity(this)

        setUpViewPager()
        setUpCircleIndicator()
    }

    private fun setUpViewPager() {

        viewBinding.mainContainer.apply {


            viewModel.getFirstStartLiveData().observe(this@MainActivity) {
                if(!it) currentItem = 1
            }

            adapter = PagerFragmentAdapter(
                this@MainActivity,
                listOf(
                    StartFragment(),
                    NativeCountersFragment(),
                    CustomCountersFragment()
                )
            )
            setPageTransformer(DepthPageTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if(position == 0) {
                        viewBinding.tabs.setViewVisibility(View.GONE)
                    } else {
                        viewBinding.tabs.setViewVisibility(View.VISIBLE)
                    }
                }
            })
        }
    }

    private fun setUpCircleIndicator() {

        viewBinding.apply {
            tabs.setViewPager(mainContainer)
            mainContainer.adapter?.registerAdapterDataObserver(tabs.adapterDataObserver)
        }
    }
}