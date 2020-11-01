package com.scootin.view.fragment.home.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.scootin.R
import com.scootin.databinding.FragmentPendingOrderDetailsBinding
import com.scootin.databinding.FragmentSettingsBinding
import com.scootin.network.AppExecutors
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrderDetailsItemAdapter
import com.scootin.view.fragment.home.orders.PendingOrderDetailsFragmentArgs
import com.scootin.viewmodel.home.UserViewModel
import com.scootin.viewmodel.order.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class SettingsFragment:Fragment(R.layout.fragment_settings) {
    private val userViewModel: UserViewModel by viewModels()

    private var binding by autoCleared<FragmentSettingsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentSettingsBinding.bind(view)
        binding.lifecycleOwner = this
        setupListeners()
    }

    private fun setupListeners() {
        Timber.i("riderid ${AppHeaders.userID}")

        userViewModel.getRiderInfo(AppHeaders.userID).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.data= it.data
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
            }
        }
    }

}