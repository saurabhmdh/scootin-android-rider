package com.scootin.view.fragment.home.dashboard


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.iid.FirebaseInstanceId
import com.scootin.R
import com.scootin.databinding.FragmentDashboardBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.viewmodel.home.DashBoardFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import androidx.lifecycle.observe
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders

import javax.inject.Inject


@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private var binding by autoCleared<FragmentDashboardBinding>()
    private val viewModel: DashBoardFragmentViewModel by viewModels()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        binding.pendingOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToPendingOrdersFragment())
        }
        binding.completedOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToCompletedOrdersFragment())
        }
        binding.acceptedOrdersTab.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionHomeFragmentToAcceptOrdersFragment())
        }

        binding.onlineBtn.setOnClickListener {
            viewModel.updateStatus(!binding.onlineBtn.isSelected)
        }

        updateFirebaseInformation()
        updateListeners()
    }

    private fun updateListeners() {
        viewModel.onlineStatus().observe(viewLifecycleOwner) {cache->
            if (cache == null) {
                binding.onlineBtn.setSelected(false)
            } else {
                when(cache.value) {
                    "true" -> {
                        binding.onlineBtn.setSelected(true)
                    }
                    "false" -> {
                        binding.onlineBtn.setSelected(false)
                    }
                }
            }
        }

        viewModel.countDeliverOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.orderDelivered.text = it.data
                }
            }
        }
    }


    private fun updateFirebaseInformation() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = it.result?.token
            Timber.i("Saurabh : Device token $token ")
            viewModel.updateFCMID(token)
        }
    }


}