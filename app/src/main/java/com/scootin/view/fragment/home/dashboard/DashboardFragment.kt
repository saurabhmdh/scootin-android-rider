package com.scootin.view.fragment.home.dashboard


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.scootin.LocationService
import com.scootin.network.api.Status
import com.scootin.network.manager.AppHeaders
import com.scootin.util.toText

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
            Timber.i("Status = ${binding.onlineBtn.isSelected}")
            viewModel.updateStatus(AppHeaders.userID, binding.onlineBtn.isSelected)
        }

        updateFirebaseInformation()
        updateListeners()
    }

    private fun updateListeners() {
        viewModel.onlineStatus().observe(viewLifecycleOwner) {cache->
            if (cache == null) {
                binding.onlineBtn.isSelected = false
            } else {
                Timber.i("Cache Status = ${cache.value.toBoolean()}")
                binding.onlineBtn.isSelected = cache.value.toBoolean().not()
            }
        }

        viewModel.countDeliverOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.orderDelivered.text = it.data.toString()
                }
            }
        }

        viewModel.countReceivedOrders(AppHeaders.userID).observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    binding.ordersReceived.text = it.data.toString()
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
    private fun logResultsToScreen(output:String) {
        val outputWithPreviousLogs = "$output\n${binding.outputTextView.text}"
        binding.outputTextView.text = outputWithPreviousLogs
    }
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                LocationService.EXTRA_LOCATION
            )

            if (location != null) {
                logResultsToScreen("Foreground location: ${location.toText()}")

            }
        }
    }


}