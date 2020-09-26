package com.scootin.view.fragment.home.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.scootin.R
import com.scootin.databinding.FragmentDashboardBinding
import com.scootin.network.AppExecutors
import com.scootin.util.fragment.autoCleared
import com.scootin.view.adapter.orders.PendingOrdersAdapter
import javax.inject.Inject

class DashboardFragment : Fragment(R.layout.fragment_dashboard){
    private var binding by autoCleared<FragmentDashboardBinding>()

    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var pendingOrdersAdapter: PendingOrdersAdapter

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
    }


}