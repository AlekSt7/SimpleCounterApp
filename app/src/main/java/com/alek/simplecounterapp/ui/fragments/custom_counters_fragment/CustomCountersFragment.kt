package com.alek.simplecounterapp.ui.fragments.custom_counters_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alek.simplecounterapp.ui.fragments.custom_counters_fragment.input_behaviors.GamingInputBehavior
import com.alek.simplecounterapp.ui.fragments.custom_counters_fragment.input_behaviors.ProductInputBehavior
import com.alek.simplecounterapp.databinding.FragmentCustomCountersBinding
import java.util.Currency
import java.util.Locale

class CustomCountersFragment : Fragment() {

    private val viewBinding: FragmentCustomCountersBinding by lazy {
        FragmentCustomCountersBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.apply {

            productCounter.setInputBehavior(
                ProductInputBehavior(
                    15.99,
                    Currency.getInstance("EUR"),
                    Locale.UK,
                    withErrorBg = true,
                    withClicked = true
                )
            )

            productCounter2.setInputBehavior(
                ProductInputBehavior(
                    157.99,
                    Currency.getInstance("JPY"),
                    Locale.JAPANESE,
                    withTransparentBg = true
                )
            )

            val gamingBehavior = GamingInputBehavior(5)
            gamingCounter.setInputBehavior(gamingBehavior)
            gamingBehavior.setComboListener { record, current ->
                if(current == record) {
                    Toast
                        .makeText(
                            context,
                            "Wow! New record: $record",
                            Toast.LENGTH_LONG
                        ).show()
                } else {
                    Toast
                        .makeText(
                            context,
                            "Not bad! Yor score: $current, record: $record",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }

}